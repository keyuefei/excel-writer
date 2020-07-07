package org.keyuefei.write.executor;

import org.apache.poi.ss.usermodel.Cell;
import org.keyuefei.enums.CellDataTypeEnum;
import org.keyuefei.exception.ExcelDataConvertException;
import org.keyuefei.write.context.WriteContext;
import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.converters.ConverterKeyBuild;
import org.keyuefei.write.metadata.head.CellData;
import org.keyuefei.write.metadata.holder.WriteHolder;
import org.keyuefei.write.metadata.property.ExcelContentProperty;

public abstract class AbstractExcelWriteExecutor implements ExcelWriteExecutor {

    protected WriteContext writeContext;

    public AbstractExcelWriteExecutor(WriteContext writeContext) {
        this.writeContext = writeContext;
    }

    protected CellData converterAndSet(WriteHolder writeHolder, ExcelContentProperty excelContentProperty, Class clazz, Cell cell, Object value) {
        if (value == null) {
            return new CellData(CellDataTypeEnum.EMPTY);
        }
        if (value instanceof String) {
            value = ((String) value).trim();
        }
        CellData cellData = convert(writeHolder, excelContentProperty, clazz, cell, value);
        if (cellData.getFormula() != null && cellData.getFormula()) {
            cell.setCellFormula(cellData.getFormulaValue());
        }
        if (cellData.getType() == null) {
            cellData.setType(CellDataTypeEnum.EMPTY);
        }

        switch (cellData.getType()) {
            case STRING:
                cell.setCellValue(cellData.getStringValue());
                return cellData;
            case BOOLEAN:
                cell.setCellValue(cellData.getBooleanValue());
                return cellData;
            case NUMBER:
                cell.setCellValue(cellData.getNumberValue().doubleValue());
                return cellData;
            case EMPTY:
                return cellData;
            default:
                throw new ExcelDataConvertException(cell.getRow().getRowNum(), cell.getColumnIndex(), cellData,
                        "Not supported data:" + value + " return type:" + cell.getCellType()
                                + "at row:" + cell.getRow().getRowNum());
        }
    }

    protected CellData convert(WriteHolder currentWriteHolder, ExcelContentProperty excelContentProperty, Class clazz, Cell cell, Object value) {
        if (value == null) {
            return new CellData(CellDataTypeEnum.EMPTY);
        }
        // This means that the user has defined the data.
        if (value instanceof CellData) {
            CellData cellDataValue = (CellData) value;
            if (cellDataValue.getType() != null) {
                return cellDataValue;
            } else {
                if (cellDataValue.getData() == null) {
                    cellDataValue.setType(CellDataTypeEnum.EMPTY);
                    return cellDataValue;
                }
            }
            CellData cellDataReturn = doConvert(currentWriteHolder, excelContentProperty, cellDataValue.getData().getClass(), cell, cellDataValue.getData());
            // The formula information is subject to user input
            if (cellDataValue.getFormula() != null) {
                cellDataReturn.setFormula(cellDataValue.getFormula());
                cellDataReturn.setFormulaValue(cellDataValue.getFormulaValue());
            }
            return cellDataReturn;
        }
        return doConvert(currentWriteHolder, excelContentProperty, clazz, cell, value);
    }

    private CellData doConvert(WriteHolder currentWriteHolder, ExcelContentProperty excelContentProperty, Class clazz, Cell cell, Object value) {
        Converter converter = null;

        if (excelContentProperty != null) {
            converter = excelContentProperty.getConverter();
        }

        if (converter == null) {
            converter = currentWriteHolder.converterMap().get(ConverterKeyBuild.buildKey(clazz));
        }
        if (converter == null) {
            throw new ExcelDataConvertException(cell.getRow().getRowNum(), cell.getColumnIndex(),
                    new CellData(CellDataTypeEnum.EMPTY), "Can not find 'Converter' support class " + clazz.getSimpleName() + ".");
        }
        CellData cellData;
        try {
            cellData = converter.convertToExcelData(value, excelContentProperty);
        } catch (Exception e) {
            throw new ExcelDataConvertException(cell.getRow().getRowNum(), cell.getColumnIndex(),
                    new CellData(CellDataTypeEnum.EMPTY), "Convert data:" + value + " error,at row:" + cell.getRow().getRowNum(), e);
        }
        if (cellData == null || cellData.getType() == null) {
            throw new ExcelDataConvertException(cell.getRow().getRowNum(), cell.getColumnIndex(),
                    new CellData(CellDataTypeEnum.EMPTY), "Convert data:" + value + " return null,at row:" + cell.getRow().getRowNum());
        }
        return cellData;
    }

}
