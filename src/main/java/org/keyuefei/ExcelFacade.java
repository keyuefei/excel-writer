package org.keyuefei;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.keyuefei.annotation.ExcelAnnotation;
import org.keyuefei.annotation.ExcelHeadAnnotation;
import org.keyuefei.annotation.ExcelHeadGroupAnnotation;
import org.keyuefei.annotation.ExcelHeadKeyAnnotation;
import org.keyuefei.condition.ColumnCondition;
import org.keyuefei.data.TestData1;
import org.keyuefei.exception.AttributeException;
import org.keyuefei.exception.GroupFieldException;
import org.keyuefei.matcher.DefaultKeyMatcher;
import org.keyuefei.matcher.KeyMatcher;
import org.keyuefei.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @Description 门面
 * @Author 003654
 * @Date 2020/6/19
 * @Time 16:46
 */
public class ExcelFacade<T> {


    private static Logger logger = Logger.getLogger(ExcelFacade.class.getName());

    public byte[] export(List<T> data, Class<?> clz) throws InstantiationException, IllegalAccessException {
        //1. 解析excel注解类信息
        Excel excel = parseExcel(clz);
        //2. 生成excel的数据
        Map<Integer, List<T>> row2data = parseExcelData(excel, data);
        //3. 生成excel bytes
        byte[] bytes = generateExcel(excel, row2data);
        return bytes;
    }


    private byte[] generateExcel(Excel excel, Map<Integer, List<T>> row2data) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(excel.getSheetName());
        Map<Integer, List<ExcelHead>> level2ExcelHeadsMap = excel.getHorizontalLevel2ExcelHeadsMap();

        int horizontalHeadTotalRows = excel.getHorizontalHeadTotalRows();

        //行头
        for (int i = excel.getRowOffset(); i < horizontalHeadTotalRows; i++) {
            Row row = sheet.createRow(i);
            List<ExcelHead> excelHeads = level2ExcelHeadsMap.get(i);
            for (ExcelHead excelHead : excelHeads) {
                if (excelHead.getColSpan() == 0) {
                    continue;
                }
                Cell cell = row.createCell(excelHead.getCol());
                cell.setCellValue(excelHead.getText());
                mergeCells(excelHead, sheet, workbook);
            }
        }

        //列头 和 数据
        List<ExcelHead> leaves = excel.getHorizontalLevel2ExcelHeadsMap().get(excel.getHorizontalHeadTotalRows() - 1);
        int verticalHeadTotalColumns = excel.getExcelHeadGroups().size();
        Map<Integer, List<ExcelHead>> verticalLevel2ExcelHeadsMap = excel.getVerticalLevel2ExcelHeadsMap();
        int verticalHeadTotalRows = excel.getVerticalLevel2ExcelHeadsMap().size();

        excel.setVerticalHeadTotalRows(verticalHeadTotalRows);
        excel.setVerticalHeadTotalColumns(verticalHeadTotalColumns);
        excel.setTotalRows(excel.getVerticalHeadTotalRows() + excel.getHorizontalHeadTotalRows());
        excel.setTotalColumns(excel.getVerticalHeadTotalColumns() + excel.getHorizontalHeadTotalColumns());

        logger.info("===========content==============");
        for (Map.Entry<Integer, List<ExcelHead>> verticalLevel2ExcelHeadsEntry : verticalLevel2ExcelHeadsMap.entrySet()) {
            int rowIndex = verticalLevel2ExcelHeadsEntry.getKey();
            List<ExcelHead> excelHeads = verticalLevel2ExcelHeadsEntry.getValue();
            Row row = sheet.createRow(rowIndex);
            for (ExcelHead excelHead : excelHeads) {
                Cell cell = row.createCell(excelHead.getCol());
                cell.setCellValue(excelHead.getText());
                mergeCells(excelHead, sheet, workbook);
                //处理数据
                generateExcelContent(row2data.get(rowIndex), leaves, row, excel);
            }
        }


        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            workbook.dispose();
        }
        return null;
    }

    private void generateExcelContent(List<T> rowGroupData, List<ExcelHead> leaves, Row row, Excel excel) {
        //确定这一行 的数据情况
        int[] data = new int[leaves.size()];

        for (T rgd : rowGroupData) {
            int colIndex = excel.getKeyMatcher().match(rgd);
//            int count = excel.getAccumulator().accumulate(colIndex, excel);
            data[colIndex] += 1;
        }

        System.out.println("行: " + row.getRowNum());
        for (int i = 0; i < data.length; i++) {
            System.out.print(data[i] + "\t");
        }
        System.out.println();


        for (int i = 0; i < data.length; i++) {
            int value = data[i];
            Cell cell = row.createCell(i + excel.getVerticalHeadTotalColumns());
            if (value == 0) {
                cell.setCellValue("");
            } else {
                cell.setCellValue(value);
            }
        }
    }

    private static void mergeCells(ExcelHead excelHead, Sheet sheet, Workbook workbook) {
        if (excelHead.getColSpan() > 1 || excelHead.getRowSpan() > 1) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(excelHead.getRow(), excelHead.getRow() + excelHead.getRowSpan() - 1,
                    excelHead.getCol(), excelHead.getCol() + excelHead.getColSpan() - 1);
            logger.info(excelHead.getText() + "，行合并【起始" + excelHead.getRow() + "-结束" + (excelHead.getRow() + excelHead.getRowSpan() - 1) + "】\t " +
                    "列合并【起始" + excelHead.getCol() + "-结束" + (excelHead.getCol() + excelHead.getColSpan() - 1) + "】");

            sheet.addMergedRegion(cellRangeAddress);
            // 下边框
            RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, workbook);
            // 左边框
            RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, workbook);
            // 右边框
            RegionUtil.setBorderRight(1, cellRangeAddress, sheet, workbook);
        }
    }


    private Map<Object, List<T>> group(List<T> data, Field field) {
        Function<T, Object> function = d -> {
            try {
                Object v = field.get(d);
                if (v == null) {
                    throw new GroupFieldException("分组属性【" + field.getName() + "】对应的字段值不能为空");
                }
                return field.get(d);
            } catch (IllegalAccessException e) {
                throw new GroupFieldException(field.getName(), e);
            }
        };
        return data.stream().collect(Collectors.groupingBy(function));
    }


    public int group2Vertical(Map<Integer, List<ExcelHead>> levelExcelHeads,
                              Map<Integer, List<T>> verticalLevelData,
                              List<T> data, List<ExcelHeadGroup> excelHeadGroups,
                              int level, int nextLevelStartRowIndex) throws GroupFieldException {

        if (level > excelHeadGroups.size() - 1) {
            return 1;
        }
        ExcelHeadGroup excelHeadGroup = excelHeadGroups.get(level);
        Field field;
        try {
            field = TestData1.class.getDeclaredField(excelHeadGroup.getKey());
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new GroupFieldException("【分组属性】没有属性：" + excelHeadGroup.getKey(), e);
        }
        Map<Object, List<T>> groupData = group(data, field);

        Set<Map.Entry<Object, List<T>>> entries = groupData.entrySet();

        Iterator<Map.Entry<Object, List<T>>> iterator = entries.iterator();
        int totalRowSpan = 0;
        while (iterator.hasNext()) {
            Map.Entry<Object, List<T>> entry = iterator.next();
            Object key = entry.getKey();
            List<T> groupData1 = entry.getValue();
            if (levelExcelHeads.get(nextLevelStartRowIndex) == null) {
                levelExcelHeads.put(nextLevelStartRowIndex, new ArrayList<>());
            }
            if (levelExcelHeads.size() - 1 == level) {
                //放入数据
                verticalLevelData.put(nextLevelStartRowIndex, groupData1);
            }
            ExcelHead excelHead = new ExcelHead();
            excelHead.setText((String) key);
            excelHead.setColSpan(1);
            excelHead.setRow(nextLevelStartRowIndex);
            excelHead.setCol(level);


            levelExcelHeads.get(nextLevelStartRowIndex).add(excelHead);

            int rowSpan = group2Vertical(levelExcelHeads, verticalLevelData, groupData1, excelHeadGroups, level + 1, nextLevelStartRowIndex);
            excelHead.setRowSpan(rowSpan);
            totalRowSpan += rowSpan;
            nextLevelStartRowIndex = rowSpan + nextLevelStartRowIndex;
        }

        return totalRowSpan;
    }

    private Map<Integer, List<T>> parseExcelData(Excel excel, List<T> data) {
        Map<Integer, List<ExcelHead>> verticalLevelExcelHeads = new HashMap<>();
        Map<Integer, List<T>> verticalLevelData = new HashMap<>();
        //将数据提取出来
        group2Vertical(verticalLevelExcelHeads, verticalLevelData, data, excel.getExcelHeadGroups(), 0, excel.getHorizontalHeadTotalRows());
        excel.setVerticalLevel2ExcelHeadsMap(verticalLevelExcelHeads);
        return verticalLevelData;
    }

    private Excel parseExcel(Class<?> clz) throws IllegalAccessException, InstantiationException {
        ExcelAnnotation excelAnnotation = clz.getAnnotation(ExcelAnnotation.class);
        if (excelAnnotation == null) {
            throw new RuntimeException("不是excel注解类，无法解析");
        }
        Field[] fields = clz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            throw new RuntimeException("excel注解类中没有表头");
        }
        Excel excel = new Excel();
        //1. 解析excel基本信息
        parseExcelBasic(excel, excelAnnotation);
        //2. 解析excel表头信息
        parseExcelHead(excel, fields);


        return excel;
    }

    private void parseExcelHead(Excel excel, Field[] fields) {
        //1. 获取表头基本信息
        List<ExcelHead> excelHeads = parseExcelHeads(fields, excel);
        //2. key: index   value : excelHead
        Map<Integer, ExcelHead> index2ExcelHeadMap = excelHeads.stream()
                .collect(Collectors.toMap(ExcelHead::getIndex, Function.identity(),
                        (oldValue, newValue) -> {
                            throw new AttributeException("【index必须唯一】" + newValue + "与" + oldValue + "的index属性重复");
                        }));
        //3. 设置表头父子关系
        parseExcelHeadRelation(excelHeads, index2ExcelHeadMap);
        //4. 将表头分级
        horizontalLevel(excelHeads, excel);
        //5. 设置表头的位置信息 rowIndex, colIndex, rowSpan, colSpan
        parseExcelHeadLocation(excel);
        //6.
        //keyMatcher
        List<ExcelHead> horizontalLeaves = excel.getHorizontalLevel2ExcelHeadsMap().get(excel.getHorizontalHeadTotalRows() - 1);
        ((DefaultKeyMatcher) excel.getKeyMatcher()).init(horizontalLeaves);

        excel.setExcelHeads(excelHeads);
    }

    private void parseExcelHeadLocation(Excel excel) {
        Map<Integer, List<ExcelHead>> horizontalLevel2ExcelHeadsMap = excel.getHorizontalLevel2ExcelHeadsMap();
        int horizontalHeadTotalRows = horizontalLevel2ExcelHeadsMap.size();
        List<ExcelHeadGroup> excelHeadGroups = new ArrayList<>();
        int horizontalHeadTotalColumns = 0;
        for (int i = horizontalHeadTotalRows - 1; i >= 0; i--) {
            List<ExcelHead> levelExcelHeads = horizontalLevel2ExcelHeadsMap.get(i);
            boolean isLeaves = i == horizontalHeadTotalRows - 1;
            if (isLeaves) {
                horizontalHeadTotalColumns = levelExcelHeads.size();
                for (int j = 0; j < horizontalHeadTotalColumns; j++) {
                    ExcelHead excelHead = levelExcelHeads.get(j);
                    excelHead.setRow(i);
                    excelHead.setCol(excel.getGroupColumns() + j);
                    excelHead.setRowSpan(1);
                    excelHead.setColSpan(1);
                }
            } else {
                int columnOffset = 0;
                for (int j = 0; j < levelExcelHeads.size(); j++) {
                    ExcelHead excelHead = levelExcelHeads.get(j);
                    List<ExcelHead> childHeads = excelHead.getChildHeads();
                    if (childHeads != null) {
                        int colSpan = childHeads.stream().mapToInt(ExcelHead::getColSpan).sum();
                        excelHead.setColSpan(colSpan);
                        excelHead.setRowSpan(1);
                        excelHead.setRow(i);
                        excelHead.setCol(excel.getGroupColumns() + columnOffset);
                        columnOffset += colSpan;
                    }
                    if (excelHead.getExcelHeadGroup() != null) {
                        excelHeadGroups.add(excelHead.getExcelHeadGroup());
                        excelHead.setRowSpan(horizontalHeadTotalRows);
                        excelHead.setColSpan(1);
                        excelHead.setRow(i);
                        excelHead.setCol(j);
                    }
                }
            }
        }

        excel.setExcelHeadGroups(excelHeadGroups);
        excel.setHorizontalHeadTotalRows(horizontalHeadTotalRows);
        excel.setHorizontalHeadTotalColumns(horizontalHeadTotalColumns);

    }

    private List<ExcelHead> parseExcelHeads(Field[] fields, Excel excel) {
        return Arrays.asList(fields).stream()
                .filter(field -> field.getAnnotation(ExcelHeadAnnotation.class) != null)
                .map(field -> {
                    String text;
                    int index, parentIndex;
                    ExcelHeadAnnotation excelHeadAnnotation = field.getAnnotation(ExcelHeadAnnotation.class);
                    if (excelHeadAnnotation != null) {
                        text = excelHeadAnnotation.text();
                        index = excelHeadAnnotation.index();
                        parentIndex = excelHeadAnnotation.parentIndex();
                        if (index == parentIndex) {
                            throw new AttributeException("【属性异常】" + excelHeadAnnotation + "中index与parentIndex相同");
                        }
                    } else {
                        throw new RuntimeException("没有必须注解！");
                    }

                    ExcelHead excelHead = new ExcelHead(text, index, parentIndex, 0, 0
                            , null, null, false, null);

                    ExcelHeadGroupAnnotation excelHeadGroupAnnotation = field.getAnnotation(ExcelHeadGroupAnnotation.class);

                    if (excelHeadGroupAnnotation != null) {
                        ExcelHeadGroup excelHeadGroup = new ExcelHeadGroup(excelHeadGroupAnnotation.key());
                        excelHead.setExcelHeadGroup(excelHeadGroup);
                        excel.setGroupColumns(excel.getGroupColumns() + 1);
                    }
                    excelHead.setRowSpan(1);
                    excelHead.setColSpan(1);

                    ExcelHeadKeyAnnotation[] excelHeadKeys = field.getAnnotationsByType(ExcelHeadKeyAnnotation.class);
                    if (excelHeadKeys != null && excelHeadKeys.length != 0) {
                        List<ExcelHeadKey> headKeys = Arrays.stream(excelHeadKeys)
                                .map(excelHeadKey -> new ExcelHeadKey(excelHeadKey.key(), excelHeadKey.value())).collect(Collectors.toList());
                        excelHead.setHeadKeys(headKeys);
                    }

                    return excelHead;
                }).collect(Collectors.toList());
    }

    private Map horizontalLevel(List<ExcelHead> excelHeads, Excel excel) {
        Map<Integer, List<ExcelHead>> horizontalLevel2ExcelHeadsMap = new HashMap(16);
        for (ExcelHead excelHead : excelHeads) {
            int level = level(excelHead);
            if (!horizontalLevel2ExcelHeadsMap.containsKey(level)) {
                horizontalLevel2ExcelHeadsMap.put(level, new ArrayList<>());
            }
            horizontalLevel2ExcelHeadsMap.get(level).add(excelHead);
        }
        excel.setHorizontalLevel2ExcelHeadsMap(horizontalLevel2ExcelHeadsMap);
        return horizontalLevel2ExcelHeadsMap;
    }


    private int level(ExcelHead excelHead) {
        int level = 0;
        while (excelHead.getParentHead() != null) {
            level++;
            excelHead = excelHead.getParentHead();
        }
        return level;
    }

    /**
     * 设置表头父子关系
     *
     * @param excelHeads
     * @param index2ExcelHeadMap
     */
    private void parseExcelHeadRelation(List<ExcelHead> excelHeads, Map<Integer, ExcelHead> index2ExcelHeadMap) {
        excelHeads.stream().sorted(Comparator.comparing(ExcelHead::getIndex))
                .forEach(excelHead -> {
                    ExcelHead parentExcelHead = index2ExcelHeadMap.get(excelHead.getParentIndex());
                    if (parentExcelHead == null) {
                        return;
                    }
                    List<ExcelHead> childHeads = parentExcelHead.getChildHeads();
                    if (childHeads == null) {
                        childHeads = new ArrayList<>();
                        parentExcelHead.setChildHeads(childHeads);
                    }
                    childHeads.add(excelHead);
                    excelHead.setParentHead(parentExcelHead);
                });

    }

    private void parseExcelBasic(Excel excel, ExcelAnnotation excelAnnotation) throws IllegalAccessException, InstantiationException {
        String sheetName = excelAnnotation.sheetName();

        Class<KeyMatcher> keyMatcherClass = (Class<KeyMatcher>) excelAnnotation.keMatcher();
        DefaultKeyMatcher defaultKeyMatcher = (DefaultKeyMatcher) keyMatcherClass.newInstance();

        Class<ColumnCondition>[] invalidColumnConditions = (Class<ColumnCondition>[]) excelAnnotation.invalidColumnCondition();
        List<ColumnCondition> columnConditions = new ArrayList<>(invalidColumnConditions.length);
        for (Class<ColumnCondition> invalidColumnCondition : invalidColumnConditions) {
            ColumnCondition columnCondition = invalidColumnCondition.newInstance();
            columnConditions.add(columnCondition);
        }
        excel.setSheetName(sheetName);
        excel.setKeyMatcher(defaultKeyMatcher);
        excel.setColumnConditions(columnConditions);
        excel.setRowOffset(excelAnnotation.rowOffset());
        excel.setColumnOffset(excelAnnotation.columnOffset());
    }


}