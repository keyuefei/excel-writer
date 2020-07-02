package org.keyuefei.write.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.keyuefei.support.ExcelTypeEnum;
import org.keyuefei.write.metadata.head.ExcelHead;
import org.keyuefei.write.metadata.holder.WriteWorkbookHolder;


public class WorkBookUtil {

    private static final int ROW_ACCESS_WINDOW_SIZE = 500;

    private WorkBookUtil() {
    }

    public static void createWorkBook(WriteWorkbookHolder writeWorkbookHolder) {
        if (ExcelTypeEnum.XLSX.equals(writeWorkbookHolder.getExcelType())) {
            Workbook workbook;
            if (writeWorkbookHolder.getInMemory()) {
                workbook = new XSSFWorkbook();
            } else {
                workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
            }
            writeWorkbookHolder.setWorkbook(workbook);
            return;
        }
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        writeWorkbookHolder.setWorkbook(hssfWorkbook);
    }

    public static Sheet createSheet(Workbook workbook, String sheetName) {
        return workbook.createSheet(sheetName);
    }

    public static Row createRow(Sheet sheet, int rowNum) {
        return sheet.createRow(rowNum);
    }

    public static Cell createCell(Row row, int colNum) {
        return row.createCell(colNum);
    }

    public static Cell createCell(Row row, int colNum, CellStyle cellStyle) {
        Cell cell = row.createCell(colNum);
        cell.setCellStyle(cellStyle);
        return cell;
    }

    public static Cell createCell(Row row, int colNum, CellStyle cellStyle, String cellValue) {
        Cell cell = createCell(row, colNum, cellStyle);
        cell.setCellValue(cellValue);
        return cell;
    }

    public static Cell createCell(Row row, int colNum, String cellValue) {
        Cell cell = row.createCell(colNum);
        cell.setCellValue(cellValue);
        return cell;
    }


    public static void mergeCells(ExcelHead excelHead, Sheet sheet) {
        if (excelHead.getColSpan() > 1 || excelHead.getRowSpan() > 1) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(excelHead.getRow(), excelHead.getRow() + excelHead.getRowSpan() - 1,
                    excelHead.getCol(), excelHead.getCol() + excelHead.getColSpan() - 1);
            sheet.addMergedRegion(cellRangeAddress);
        }
    }
}
