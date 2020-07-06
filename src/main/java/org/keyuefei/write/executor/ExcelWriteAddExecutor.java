package org.keyuefei.write.executor;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.keyuefei.exception.GroupFieldException;
import org.keyuefei.write.accumulator.Accumulator;
import org.keyuefei.write.context.WriteContext;
import org.keyuefei.write.matcher.DefaultKeyMatcher;
import org.keyuefei.write.matcher.KeyMatcher;
import org.keyuefei.write.metadata.head.ExcelHead;
import org.keyuefei.write.metadata.head.ExcelHeadGroup;
import org.keyuefei.write.metadata.head.ExcelMatchKey;
import org.keyuefei.write.metadata.property.ExcelContentProperty;
import org.keyuefei.write.metadata.property.ExcelWriteHeadProperty;
import org.keyuefei.write.util.WorkBookUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

import static org.keyuefei.write.constant.Constant.SEPARATOR;


public class ExcelWriteAddExecutor extends AbstractExcelWriteExecutor {


    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriteAddExecutor.class);


    private Map<Integer, List<ExcelHead>> heads;

    private Map<Integer, List<ExcelHead>> contentHeads;

    private Map<Integer, Object> contents;

    private Class contentClass;

    private KeyMatcher keyMatcher;

    private Accumulator accumulator;


    public ExcelWriteAddExecutor(WriteContext writeContext) {
        super(writeContext);
    }

    public void add(List data) {
        if (!initDataClass(data)) {
            return;
        }
        ExcelWriteHeadProperty excelWriteHeadProperty = writeContext.writeSheetHolder().getExcelWriteHeadProperty();
        init(excelWriteHeadProperty, data);

        add(excelWriteHeadProperty);
    }

    private void add(ExcelWriteHeadProperty excelWriteHeadProperty) {
        //head
        int headRows = heads.size();
        for (int rowIndex = 0; rowIndex < headRows; rowIndex++) {
            Row row = WorkBookUtil.createRow(writeContext.writeSheetHolder().getSheet(), rowIndex);
            List<ExcelHead> excelHeads = heads.get(rowIndex);
            for (ExcelHead excelHead : excelHeads) {
                if (excelHead.getColSpan() == 0 || excelHead.getRowSpan() == 0) {
                    continue;
                }
                WorkBookUtil.createCell(row, excelHead.getCol(), excelHead.getText());
                WorkBookUtil.mergeCells(excelHead, writeContext.writeSheetHolder().getSheet());
            }
        }

        if (excelWriteHeadProperty.isNeedGroup()) {
            //content head
            for (Map.Entry<Integer, List<ExcelHead>> contentHead : contentHeads.entrySet()) {
                int rowIndex = contentHead.getKey();
                List<ExcelHead> excelHeads = contentHead.getValue();
                Row row = WorkBookUtil.createRow(writeContext.writeSheetHolder().getSheet(), rowIndex);
                for (ExcelHead excelHead : excelHeads) {
                    WorkBookUtil.createCell(row, excelHead.getCol(), excelHead.getText());
                    WorkBookUtil.mergeCells(excelHead, writeContext.writeSheetHolder().getSheet());
                    addContent(contents.get(rowIndex), row);
                }
            }
        } else {
            for (Map.Entry<Integer, Object> content : contents.entrySet()) {
                Row row = WorkBookUtil.createRow(writeContext.writeSheetHolder().getSheet(), content.getKey());
                addContent(content.getValue(), row);
            }
        }

    }

    private void addContent(Object content, Row row) {
        boolean needGroup = writeContext.writeSheetHolder().getExcelWriteHeadProperty().isNeedGroup();
        if (needGroup && content instanceof List) {
            addGroupContent((List) content, row);
        } else {
            addNormalContent(content, row);
        }
    }

    private void addNormalContent(Object content, Row row) {
        //todo 待提升效率
        Map<Integer, Field> sortedFiledMap = writeContext.writeSheetHolder().getExcelWriteHeadProperty().getSortedAllFiledMap();
        Map<Integer, ExcelContentProperty> contentPropertyMap = writeContext.writeSheetHolder().getExcelWriteHeadProperty().getContentPropertyMap();
        for (Map.Entry<Integer, Field> filedEntry : sortedFiledMap.entrySet()) {
            Field field = filedEntry.getValue();
            int colIndex = filedEntry.getKey();
            field.setAccessible(true);
            Object value = null;
            try {
                value = field.get(content);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            doAddBasicTypeToExcel(value, row, contentPropertyMap.get(colIndex), colIndex);
        }
    }

    private void addGroupContent(List content, Row row) {
        Map<Integer, Object> col2content = new HashMap<>(16);
        //确定数据的 colIndex
        for (Object c : content) {
            int colIndex = keyMatcher.match(c);
            Object value;
            if (col2content.containsKey(colIndex)) {
                Object oldValue = col2content.get(colIndex);
                value = accumulator.accumulate(row.getRowNum(), colIndex, oldValue, c);
            } else {
                value = accumulator.accumulate(row.getRowNum(), colIndex, c);
            }
            col2content.put(colIndex, value);
        }
        //添加内容
        for (Map.Entry<Integer, Object> entry : col2content.entrySet()) {
            int colIndex = entry.getKey();
            Object value = entry.getValue();
            doAddBasicTypeToExcel(value, row, null, colIndex);
        }
    }

    private void doAddBasicTypeToExcel(Object value, Row row, ExcelContentProperty excelContentProperty, int colIndex) {
        Cell cell = WorkBookUtil.createCell(row, colIndex);
        converterAndSet(writeContext.currentWriteHolder(), excelContentProperty, value.getClass(), cell, value);
    }

    private boolean initDataClass(List data) {
        if (data == null || data.size() == 0) {
            return false;
        }
        contentClass = data.get(0).getClass();
        return true;
    }

    private void init(ExcelWriteHeadProperty headProperty, List data) {
        heads = horizontalLevel(headProperty.getHeads());
        accumulator = writeContext.writeSheetHolder().getAccumulator();
        keyMatcher = initKeyMatcher();

        //数据集合 key: rowIndex  value: 数据集合
        contents = new HashMap<>(16);
        if (headProperty.isNeedGroup()) {
            //分组后的  内容表头 key: rowIndex  value: 表头
            contentHeads = new HashMap<>(16);
            //将数据提取出来
            group2Vertical(contentHeads, contents, data, headProperty.getGroups(), 0, headProperty.getHeadRowNumber());
        } else {
            //没有分组
            for (int i = 0, rowIndex = headProperty.getHeadRowNumber(); i < data.size(); i++, rowIndex++) {
                contents.put(rowIndex, data.get(i));
            }
        }


    }

    private KeyMatcher initKeyMatcher() {
        ExcelWriteHeadProperty headProperty = writeContext.writeSheetHolder().getExcelWriteHeadProperty();

        //todo
        List<List<ExcelMatchKey>> matchKeys = headProperty.getMatchKeys();
        int groupSize = headProperty.isNeedGroup() ? headProperty.getGroups().size() : 0;

        Map<List<String>, Integer> keys2colIndexMap = new HashMap(16);
        //需要保证顺序
        Set<String> keys = new LinkedHashSet<>();
        for (int colIndex = 0; colIndex < matchKeys.size(); colIndex++) {
            List<ExcelMatchKey> matches = matchKeys.get(colIndex);
            //排序
            matches.sort(Comparator.comparing(ExcelMatchKey::getKey));

            List<String> tmpKeys = new ArrayList<>();
            for (ExcelMatchKey match : matches) {
                keys.add(match.getKey());
                tmpKeys.add(match.getKey() + SEPARATOR + match.getValue());
            }
            keys2colIndexMap.put(tmpKeys, colIndex + groupSize);
        }

        return new DefaultKeyMatcher(keys2colIndexMap, keys);
    }


    private Map horizontalLevel(List<ExcelHead> excelHeads) {
        Map<Integer, List<ExcelHead>> horizontalLevel2ExcelHeadsMap = new HashMap(16);
        for (ExcelHead excelHead : excelHeads) {
            int level = level(excelHead);
            if (!horizontalLevel2ExcelHeadsMap.containsKey(level)) {
                horizontalLevel2ExcelHeadsMap.put(level, new ArrayList<>());
            }
            horizontalLevel2ExcelHeadsMap.get(level).add(excelHead);
        }
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


    public int group2Vertical(Map<Integer, List<ExcelHead>> contentHeads, Map<Integer, Object> content,
                              List data, List<ExcelHeadGroup> groups, int level, int nextLevelStartRowIndex) throws GroupFieldException {

        if (level > groups.size() - 1) {
            return 1;
        }

        Map<Object, List> groupData = group(data, groups.get(level));

        Set<Map.Entry<Object, List>> entries = groupData.entrySet();

        Iterator<Map.Entry<Object, List>> iterator = entries.iterator();
        int totalRowSpan = 0;
        while (iterator.hasNext()) {
            Map.Entry<Object, List> entry = iterator.next();
            Object key = entry.getKey();
            List groupData1 = entry.getValue();
            if (contentHeads.get(nextLevelStartRowIndex) == null) {
                contentHeads.put(nextLevelStartRowIndex, new ArrayList<>());
            }
            if (groups.size() - 1 == level) {
                //放入数据
                content.put(nextLevelStartRowIndex, groupData1);
            }
            ExcelHead excelHead = new ExcelHead();
            excelHead.setText((String) key);
            excelHead.setColSpan(1);
            excelHead.setRow(nextLevelStartRowIndex);
            excelHead.setCol(level);


            contentHeads.get(nextLevelStartRowIndex).add(excelHead);

            int rowSpan = group2Vertical(contentHeads, content, groupData1, groups, level + 1, nextLevelStartRowIndex);
            excelHead.setRowSpan(rowSpan);
            totalRowSpan += rowSpan;
            nextLevelStartRowIndex = rowSpan + nextLevelStartRowIndex;
        }
        return totalRowSpan;
    }

    private Map<Object, List> group(List data, ExcelHeadGroup headGroup) {
        Map<Object, List> groupByField = new LinkedHashMap<>();
        try {
            Field field = contentClass.getDeclaredField(headGroup.getKey());
            field.setAccessible(true);
            for (Object d : data) {
                Object v = field.get(d);
                if (v == null) {
                    throw new GroupFieldException("分组属性【" + field.getName() + "】对应的字段值不能为空");
                }
                if (!groupByField.containsKey(v)) {
                    groupByField.put(v, new ArrayList());
                }
                groupByField.get(v).add(d);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new GroupFieldException(e);
        }
        return groupByField;
    }


}
