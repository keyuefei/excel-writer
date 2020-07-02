package org.keyuefei.write.context;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.keyuefei.exception.ExcelGenerateException;
import org.keyuefei.write.metadata.WriteSheet;
import org.keyuefei.write.metadata.WriteWorkbook;
import org.keyuefei.write.metadata.holder.WriteSheetHolder;
import org.keyuefei.write.metadata.holder.WriteWorkbookHolder;
import org.keyuefei.write.util.WorkBookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WriteContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(WriteContext.class);

    private WriteWorkbookHolder writeWorkbookHolder;

    private WriteSheetHolder writeSheetHolder;

    private boolean finished = false;

    public WriteContext(WriteWorkbook writeWorkbook) {
        if (writeWorkbook == null) {
            throw new IllegalArgumentException("Workbook argument cannot be null");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Begin to Initialization 'WriteContextImpl'");
        }
        initCurrentWorkbookHolder(writeWorkbook);
        try {
            WorkBookUtil.createWorkBook(writeWorkbookHolder);
        } catch (Exception e) {
            throw new ExcelGenerateException("Create workbook failure", e);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialization 'WriteContextImpl' complete");
        }
    }

    private void initCurrentWorkbookHolder(WriteWorkbook writeWorkbook) {
        writeWorkbookHolder = new WriteWorkbookHolder(writeWorkbook);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeWorkbookHolder");
        }
    }

    /**
     * @param writeSheet
     */
    public void currentSheet(WriteSheet writeSheet) {
        if (writeSheet == null) {
            throw new IllegalArgumentException("Sheet argument cannot be null");
        }

        initCurrentSheetHolder(writeSheet);

        initSheet();
    }


    private void initCurrentSheetHolder(WriteSheet writeSheet) {
        writeSheetHolder = new WriteSheetHolder(writeSheet, writeWorkbookHolder);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CurrentConfiguration is writeSheetHolder");
        }
    }

    private void initSheet() {
        writeSheetHolder.setSheet(createSheet());
    }

    private Sheet createSheet() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Can not find sheet:{} ,now create it", writeSheetHolder.getSheetNo());
        }
        if (StringUtils.isEmpty(writeSheetHolder.getSheetName())) {
            writeSheetHolder.setSheetName(writeSheetHolder.getSheetNo().toString());
        }
        Sheet currentSheet =
                WorkBookUtil.createSheet(writeWorkbookHolder.getWorkbook(), writeSheetHolder.getSheetName());
        return currentSheet;
    }




    public void finish(boolean onException) {
        if (finished) {
            return;
        }
        finished = true;
        if (writeWorkbookHolder == null) {
            return;
        }
        Throwable throwable = null;
        // Determine if you need to write excel
        boolean writeExcel = !onException;
        if (writeWorkbookHolder.getWriteExcelOnException()) {
            writeExcel = Boolean.TRUE;
        }

        try {
            if (writeExcel) {
                writeWorkbookHolder.getWorkbook().write(writeWorkbookHolder.getOutputStream());
            }
        } catch (Throwable t) {
            throwable = t;
        }

        try {
            Workbook workbook = writeWorkbookHolder.getWorkbook();
            if (workbook instanceof SXSSFWorkbook) {
                ((SXSSFWorkbook) workbook).dispose();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        try {
            if (writeWorkbookHolder.getAutoCloseStream() && writeWorkbookHolder.getOutputStream() != null) {
                writeWorkbookHolder.getOutputStream().close();
            }
        } catch (Throwable t) {
            throwable = t;
        }
        if (throwable != null) {
            throw new ExcelGenerateException("Can not close IO.", throwable);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Finished write.");
        }
    }


    public WriteWorkbookHolder writeWorkbookHolder() {
        return writeWorkbookHolder;
    }


    public WriteSheetHolder writeSheetHolder() {
        return writeSheetHolder;
    }

}
