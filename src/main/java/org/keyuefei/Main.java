package org.keyuefei;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.keyuefei.annotation.ExcelAnnotation;
import org.keyuefei.annotation.ExcelHeadAnnotation;
import org.keyuefei.annotation.ExcelLeafHeadAnnotation;
import org.keyuefei.condition.ColumnCondition;
import org.keyuefei.data.TestData1;
import org.keyuefei.exception.AttributeException;
import org.keyuefei.exception.GroupFieldException;
import org.keyuefei.matcher.KeyMatcher;
import org.keyuefei.model.BoxCubicle;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Description 主测试类
 * @Author 003654
 * @Date 2020/6/12
 * @Time 22:56
 */
public class Main {

    static void throwIOException(Integer integer) throws IOException {
    }


    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        //1. 获取excel头对象
        Excel excel = parseExcel(BoxCubicle.class);
        //3. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //4. 生成excel的content
        parseExcelContent(excel, data, new String[]{"region", "boxCityName", "supplierName"});

        //2. 获取excel的二进制
        byte[] bytes = generateExcel(excel);


        if (bytes != null) {
            String path = "D:\\keyuefei\\project\\dynamic-excel\\src\\main\\java\\org\\keyuefei\\test.xlsx";
            Files.write(Paths.get(path), bytes);
        }
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


    public static int group(Map<Integer, List<ExcelHead>> levelExcelHeads,
                            List<TestData1> data, String[] groupProperties, int level) throws GroupFieldException {
        if (level > groupProperties.length - 1) {
            return 1;
        }
        String groupProperty = groupProperties[level];
        Field field;
        try {
            field = TestData1.class.getDeclaredField(groupProperty);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new GroupFieldException("【分组属性】没有属性：" + groupProperty, e);
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
            int rowSpan = group(levelExcelHeads, groupData1, groupProperties, level + 1);
            excelHead.setRowSpan(rowSpan);

            totalRowSpan += rowSpan;
        }
        return totalRowSpan;
    }

    private static void parseExcelContent(Excel excel, List<TestData1> data, String[] groupProperties) {
        KeyMatcher keyMatcher = excel.getKeyMatcher();
        List<ExcelHead> leaves = excel.getLevel2ExcelHeadsMap().get(excel.getTotalRows() - 1);

        //按照指定顺序分组
        Map<Integer, List<ExcelHead>> levelExcelHeads = new HashMap<>();
        group(levelExcelHeads, data, groupProperties, 0);

        int verticalHeadTotalColumns = levelExcelHeads.size();
        List<ExcelHead> leafExcelHeads = levelExcelHeads.get(verticalHeadTotalColumns - 1);
        int verticalHeadTotalRows = leafExcelHeads.size();

        excel.setVerticalHeadTotalRows(verticalHeadTotalRows);
        excel.setVerticalHeadTotalColumns(verticalHeadTotalColumns);

        excel.setTotalRows(excel.getVerticalHeadTotalRows() + excel.getHorizontalHeadTotalRows());
        excel.setTotalColumns(excel.getVerticalHeadTotalColumns() + excel.getHorizontalHeadTotalColumns());


        for (ExcelHead leafExcelHead : leafExcelHeads) {
            List<TestData1> rowGroupData = leafExcelHead.getRowGroupData();
            for (TestData1 rgd : rowGroupData) {
                for (ExcelHead leaf : leaves) {
                    if (keyMatcher.match(leaf.getKey(), rgd)) {


                    }
                }
            }
        }


    }


    private static byte[] generateExcel(Excel excel) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet(excel.getSheetName());
        Map<Integer, List<ExcelHead>> level2ExcelHeadsMap = excel.getLevel2ExcelHeadsMap();

        int totalRows = excel.getTotalRows();

        for (int i = excel.getRowOffset(); i < totalRows; i++) {
            Row row = sheet.createRow(i);
            List<ExcelHead> excelHeads = level2ExcelHeadsMap.get(i);
            int levelColumnStartIndex = excel.getColumnOffset();
            for (ExcelHead excelHead : excelHeads) {
                Cell cell = row.createCell(levelColumnStartIndex);
                cell.setCellValue(excelHead.getText());
                if (excelHead.getColSpan() == 0) {
                    continue;
                }

                int levelColumnEndIndex = levelColumnStartIndex + excelHead.getColSpan() - 1;

                CellRangeAddress cellRangeAddress = new CellRangeAddress(i, i,
                        levelColumnStartIndex, levelColumnEndIndex);
                sheet.addMergedRegion(cellRangeAddress);
                // 下边框
                RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, workbook);
                // 左边框
                RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, workbook);
                // 右边框
                RegionUtil.setBorderRight(1, cellRangeAddress, sheet, workbook);

                levelColumnStartIndex = levelColumnEndIndex + 1;
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

    private static Excel parseExcel(Class<BoxCubicle> clz) throws RuntimeException, IllegalAccessException, InstantiationException {
        ExcelAnnotation excelAnnotation = clz.getAnnotation(ExcelAnnotation.class);
        if (excelAnnotation == null) {
            return null;
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
                    ExcelLeafHeadAnnotation excelLeafHeadAnnotation = field.getAnnotation(ExcelLeafHeadAnnotation.class);
                    ExcelHeadAnnotation excelHeadAnnotation = field.getAnnotation(ExcelHeadAnnotation.class);
                    if (excelLeafHeadAnnotation != null) {
                        text = excelLeafHeadAnnotation.text();
                        index = excelLeafHeadAnnotation.index();
                        parentIndex = excelLeafHeadAnnotation.parentIndex();
                    } else if (excelHeadAnnotation != null) {
                        text = excelHeadAnnotation.text();
                        index = excelHeadAnnotation.index();
                        parentIndex = excelHeadAnnotation.parentIndex();
                    } else {
                        throw new RuntimeException("没有必须注解！");
                    }
                    ExcelHead excelHead = new ExcelHead(text, index, parentIndex, 0, 0,
                            null, null, null, false, null);
                    return excelHead;
                }).collect(Collectors.toList());
        //index2ExcelHeadMap: key = index, value = excelHead
        Map<Integer, ExcelHead> index2ExcelHeadMap = excelHeads.stream()
                .collect(Collectors.toMap(ExcelHead::getIndex, Function.identity(),
                        (oldValue, newValue) -> {
                            throw new AttributeException("【index必须唯一】" + newValue + "与" + oldValue + "的index属性重复");
                        }));
//        inject parentHead property
        excelHeads.stream()
                .forEach(excelHead -> {
                    ExcelHead parentExcelHead = index2ExcelHeadMap.get(excelHead.parentIndex);
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
        int horizontalHeadTotalColumns = 0;
        for (int i = horizontalHeadTotalRows - 1; i >= 0; i--) {
            List<ExcelHead> levelExcelHeads = level2ExcelHeadsMap.get(i);
            boolean isLeaves = i == horizontalHeadTotalRows - 1;
            if (isLeaves) {
//                leaf colSpan
                levelExcelHeads.forEach(excelHead -> {
                    excelHead.setColSpan(1);
                });
                horizontalHeadTotalColumns = levelExcelHeads.size();
            } else {
                levelExcelHeads.forEach(excelHead -> {
                    List<ExcelHead> childHeads = excelHead.getChildHeads();
                    int colSpan = childHeads.stream().mapToInt(ExcelHead::getColSpan).sum();
                    excelHead.setColSpan(colSpan);
                });
            }
        }
        return new Excel(excelHeads, level2ExcelHeadsMap, sheetName, horizontalHeadTotalRows, horizontalHeadTotalColumns,
                0, 0, 0, 0, excelAnnotation.rowOffset(), excelAnnotation.columnOffset(),
                columnConditions, keyMatcher);
    }

    private static int level(ExcelHead excelHead) {
        int level = 0;
        while (excelHead.getParentHead() != null) {
            level++;
            excelHead = excelHead.getParentHead();
        }
        return level;
    }


    @Data
    static class ExcelHead {
        private String text;
        private int index;
        private int parentIndex;

        //合并列
        private int colSpan;
        //合并行
        private int rowSpan;

        //父表头
        private ExcelHead parentHead;
        //子表头
        private List<ExcelHead> childHeads;

        //横向分组 数据； 横向表头有值
        private List<TestData1> rowGroupData;

        /**
         * 水平叶子节点
         */
        private boolean isHorizontalLeaf;
        /**
         * key， 水平叶子节点才有该属性
         */
        private String key;

        public ExcelHead(String text, int index, int parentIndex, int colSpan, int rowSpan,
                         ExcelHead parentHead, List<ExcelHead> childHeads, List<TestData1> rowGroupData,
                         boolean isHorizontalLeaf, String key) {
            this.text = text;
            this.index = index;
            this.parentIndex = parentIndex;
            this.colSpan = colSpan;
            this.rowSpan = rowSpan;
            this.parentHead = parentHead;
            this.childHeads = childHeads;
            this.rowGroupData = rowGroupData;
            this.isHorizontalLeaf = isHorizontalLeaf;
            this.key = key;
        }

        @Override
        public String toString() {
            return "ExcelHead{" +
                    "text='" + text + '\'' +
                    ", index=" + index +
                    ", parentIndex=" + parentIndex +
                    ", colSpan=" + colSpan +
                    ", rowSpan=" + rowSpan +
                    ", parentHead=" + parentHead +
                    '}';
        }
    }

    @Data
    static class Excel {
        private List<ExcelHead> excelHeads;
        private Map<Integer, List<ExcelHead>> level2ExcelHeadsMap;
        private String sheetName;
        //水平表头总行数
        private int horizontalHeadTotalRows;
        //水平表头总列数
        private int horizontalHeadTotalColumns;
        //垂直表头总行数
        private int verticalHeadTotalRows;
        //垂直表头总列数
        private int verticalHeadTotalColumns;
        //总行数
        private int totalRows;
        //总列数
        private int totalColumns;

        //行偏移
        private int rowOffset;
        //列偏移
        private int columnOffset;

        private List<ColumnCondition> columnConditions;
        private KeyMatcher keyMatcher;


        public Excel(List<ExcelHead> excelHeads, Map<Integer, List<ExcelHead>> level2ExcelHeadsMap, String sheetName,
                     int horizontalHeadTotalRows, int horizontalHeadTotalColumns,
                     int verticalHeadTotalRows, int verticalHeadTotalColumns,
                     int totalRows, int totalColumns,
                     int rowOffset, int columnOffset,
                     List<ColumnCondition> columnConditions, KeyMatcher keyMatcher) {
            this.excelHeads = excelHeads;
            this.level2ExcelHeadsMap = level2ExcelHeadsMap;
            this.sheetName = sheetName;
            this.horizontalHeadTotalRows = horizontalHeadTotalRows;
            this.horizontalHeadTotalColumns = horizontalHeadTotalColumns;
            this.verticalHeadTotalRows = verticalHeadTotalRows;
            this.verticalHeadTotalColumns = verticalHeadTotalColumns;
            this.totalRows = totalRows;
            this.totalColumns = totalColumns;
            this.rowOffset = rowOffset;
            this.columnOffset = columnOffset;
            this.columnConditions = columnConditions;
            this.keyMatcher = keyMatcher;
        }
    }


}

