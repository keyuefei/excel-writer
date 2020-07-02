package org.keyuefei.write;

import org.keyuefei.write.builder.ExcelBuilder;
import org.keyuefei.write.metadata.WriteSheet;
import org.keyuefei.write.metadata.WriteWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ExcelWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriter.class);

    private ExcelBuilder excelBuilder;

    /**
     * Create new writer
     *
     * @param writeWorkbook
     */
    public ExcelWriter(WriteWorkbook writeWorkbook) {
        excelBuilder = new ExcelBuilder(writeWorkbook);
    }


    public ExcelWriter write(List data, WriteSheet writeSheet) {
        excelBuilder.addContent(data, writeSheet);
        return this;
    }


    /**
     * Close IO
     */
    public void finish() {
        if (excelBuilder != null) {
            excelBuilder.finish(false);
        }
    }

    /**
     * Prevents calls to {@link #finish} from freeing the cache
     *
     */
    @Override
    protected void finalize() {
        try {
            finish();
        } catch (Throwable e) {
            LOGGER.warn("Destroy object failed", e);
        }
    }
}
