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
import org.keyuefei.matcher.KeyMatcher;
import org.keyuefei.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @Description 主测试类
 * @Author 003654
 * @Date 2020/6/12
 * @Time 22:56
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {

        //3. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //1. 获取excel头对象
        Excel excel = parseExcel(BoxCubicle.class);
        //4. 生成excel的content
        parseExcelContent(excel, data);

        //2. 获取excel的二进制
        byte[] bytes = generateExcel(excel);

        //统计信息输出
        statistics(excel);
        if (bytes != null) {
            String path = "D:\\keyuefei\\project\\dynamic-excel\\src\\main\\java\\org\\keyuefei\\test.xlsx";
            Files.write(Paths.get(path), bytes);
        }
    }

    private static void statistics(Excel excel) {
        int totalMatches = excel.getKeyMatcher().getTotalMatches();
        logger.info("总匹配行数：" + totalMatches);
    }


    private static Map<Object, List<TestData1>> group(List<TestData1> data, Field field) {
        Function<TestData1, Object> function = d -> {
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


    public static int group2Horizontal(Map<Integer, List<ExcelHead>> levelExcelHeads,
                                       List<TestData1> data, List<ExcelHeadGroup> excelHeadGroups, int level) throws GroupFieldException {
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
        Map<Object, List<TestData1>> groupData = group(data, field);

        Set<Map.Entry<Object, List<TestData1>>> entries = groupData.entrySet();
        int totalRowSpan = 0;
        for (Map.Entry<Object, List<TestData1>> entry : entries) {
            Object key = entry.getKey();
            List<TestData1> groupData1 = entry.getValue();
            if (levelExcelHeads.get(level) == null) {
                levelExcelHeads.put(level, new ArrayList<>());
            }
            ExcelHead excelHead = new ExcelHead();
            excelHead.setRowGroupData(groupData1);
            levelExcelHeads.get(level).add(excelHead);

            excelHead.setText((String) key);
            int rowSpan = group2Horizontal(levelExcelHeads, groupData1, excelHeadGroups, level + 1);
            excelHead.setRowSpan(rowSpan);

            totalRowSpan += rowSpan;
        }
        return totalRowSpan;
    }


    public static int group2Vertical(Map<Integer, List<ExcelHead>> levelExcelHeads,
                                     List<TestData1> data, List<ExcelHeadGroup> excelHeadGroups, int level, int nextLevelStartRowIndex) throws GroupFieldException {

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
        Map<Object, List<TestData1>> groupData = group(data, field);

        Set<Map.Entry<Object, List<TestData1>>> entries = groupData.entrySet();

        Iterator<Map.Entry<Object, List<TestData1>>> iterator = entries.iterator();
        int totalRowSpan = 0;
        while (iterator.hasNext()) {
            Map.Entry<Object, List<TestData1>> entry = iterator.next();
            Object key = entry.getKey();
            List<TestData1> groupData1 = entry.getValue();
            if (levelExcelHeads.get(nextLevelStartRowIndex) == null) {
                levelExcelHeads.put(nextLevelStartRowIndex, new ArrayList<>());
            }
            ExcelHead excelHead = new ExcelHead();
            excelHead.setText((String) key);
            excelHead.setColSpan(1);
            excelHead.setRow(nextLevelStartRowIndex);
            excelHead.setCol(level);
            excelHead.setRowGroupData(groupData1);

            levelExcelHeads.get(nextLevelStartRowIndex).add(excelHead);

            int rowSpan = group2Vertical(levelExcelHeads, groupData1, excelHeadGroups, level + 1, nextLevelStartRowIndex);
            excelHead.setRowSpan(rowSpan);
            totalRowSpan += rowSpan;
            nextLevelStartRowIndex = rowSpan + nextLevelStartRowIndex;
        }

        return totalRowSpan;
    }


    private static void parseExcelContent(Excel excel, List<TestData1> data) {

        //按照指定顺序分组, key：rowIndex
        Map<Integer, List<ExcelHead>> verticalLevelExcelHeads = new HashMap<>();
//        group2Horizontal(verticalLevelExcelHeads, data, excel.getExcelHeadGroups(), 0);
        group2Vertical(verticalLevelExcelHeads, data, excel.getExcelHeadGroups(), 0, excel.getHorizontalHeadTotalRows());
        excel.setVerticalLevel2ExcelHeadsMap(verticalLevelExcelHeads);

    }


    private static byte[] generateExcel(Excel excel) {
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
                if (excelHead.getCol() == verticalHeadTotalColumns - 1) {
                    //垂直叶子节点， 额外处理 groupData
                    generateExcelContent(excelHead, leaves, row, excel);
                }
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

    private static void generateExcelContent(ExcelHead excelHead, List<ExcelHead> leaves, Row row, Excel excel) {
        List<TestData1> rowGroupData = excelHead.getRowGroupData();
        //确定这一行 的数据情况
        int[] data = new int[leaves.size()];

        for (TestData1 rgd : rowGroupData) {
            for (int j = 0; j < leaves.size(); j++) {
                ExcelHead leaf = leaves.get(j);
                if (excel.getKeyMatcher().match(leaf.getHeadKeys(), rgd)) {
                    data[j]++;
                }
            }
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

    private static Excel parseExcel(Class<BoxCubicle> clz) throws RuntimeException, IllegalAccessException, InstantiationException {
        ExcelAnnotation excelAnnotation = clz.getAnnotation(ExcelAnnotation.class);
        Excel excel = new Excel();

        if (excelAnnotation == null) {
            return excel;
        }
        String sheetName = excelAnnotation.sheetName();

        Class<KeyMatcher> keyMatcherClass = (Class<KeyMatcher>) excelAnnotation.keMatcher();
        KeyMatcher keyMatcher = keyMatcherClass.newInstance();

        Class<ColumnCondition>[] invalidColumnConditions = (Class<ColumnCondition>[]) excelAnnotation.invalidColumnCondition();
        List<ColumnCondition> columnConditions = new ArrayList<>(invalidColumnConditions.length);
        for (Class<ColumnCondition> invalidColumnCondition : invalidColumnConditions) {
            ColumnCondition columnCondition = invalidColumnCondition.newInstance();
            columnConditions.add(columnCondition);
        }

        Field[] fields = clz.getDeclaredFields();
        //package excelHeads todo: check ExcelHeadAnnotation 一些属性； warn
        List<ExcelHead> excelHeads = Arrays.asList(fields).stream()
                .filter(field -> field.getAnnotation(ExcelHeadAnnotation.class) != null)
                .map(field -> {
                    String text = null;
                    int index = 0, parentIndex = 0;
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

                    ExcelHead excelHead = new ExcelHead(text, index, parentIndex, 0, 0,
                            null, null, null, false, null);

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
        //index2ExcelHeadMap: key = index, value = excelHead
        Map<Integer, ExcelHead> index2ExcelHeadMap = excelHeads.stream()
                .collect(Collectors.toMap(ExcelHead::getIndex, Function.identity(),
                        (oldValue, newValue) -> {
                            throw new AttributeException("【index必须唯一】" + newValue + "与" + oldValue + "的index属性重复");
                        }));
//        inject parentHead property
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

        //分级
        Map<Integer, List<ExcelHead>> level2ExcelHeadsMap = new HashMap(16);
        for (ExcelHead excelHead : excelHeads) {
            int level = level(excelHead);
            if (!level2ExcelHeadsMap.containsKey(level)) {
                level2ExcelHeadsMap.put(level, new ArrayList<>());
            }
            level2ExcelHeadsMap.get(level).add(excelHead);
        }
        //head total columns, rows(level)
        int horizontalHeadTotalRows = level2ExcelHeadsMap.size();
        List<ExcelHeadGroup> excelHeadGroups = new ArrayList<>();
        int horizontalHeadTotalColumns = 0;
        for (int i = horizontalHeadTotalRows - 1; i >= 0; i--) {
            List<ExcelHead> levelExcelHeads = level2ExcelHeadsMap.get(i);
            boolean isLeaves = i == horizontalHeadTotalRows - 1;
            if (isLeaves) {
                horizontalHeadTotalColumns = levelExcelHeads.size();
                for (int j = 0; j < levelExcelHeads.size(); j++) {
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
        excel.setExcelHeads(excelHeads);
        excel.setHorizontalLevel2ExcelHeadsMap(level2ExcelHeadsMap);
        excel.setSheetName(sheetName);
        excel.setHorizontalHeadTotalRows(horizontalHeadTotalRows);
        excel.setHorizontalHeadTotalColumns(horizontalHeadTotalColumns);
        excel.setRowOffset(excelAnnotation.rowOffset());
        excel.setColumnOffset(excelAnnotation.columnOffset());
        excel.setColumnConditions(columnConditions);
        excel.setKeyMatcher(keyMatcher);
        return excel;
    }

    private static int level(ExcelHead excelHead) {
        int level = 0;
        while (excelHead.getParentHead() != null) {
            level++;
            excelHead = excelHead.getParentHead();
        }
        return level;
    }


}

