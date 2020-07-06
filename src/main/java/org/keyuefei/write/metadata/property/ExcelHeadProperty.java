package org.keyuefei.write.metadata.property;

import org.apache.commons.lang3.StringUtils;
import org.keyuefei.annotation.ExcelGroup;
import org.keyuefei.annotation.ExcelMatch;
import org.keyuefei.annotation.ExcelProperty;
import org.keyuefei.annotation.format.DateTimeFormat;
import org.keyuefei.annotation.format.NumberFormat;
import org.keyuefei.exception.ExcelCommonException;
import org.keyuefei.write.converters.AutoConverter;
import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.head.ExcelHead;
import org.keyuefei.write.metadata.head.ExcelHeadGroup;
import org.keyuefei.write.metadata.head.ExcelMatchKey;
import org.keyuefei.write.metadata.holder.AbstractWriteHolder;
import org.keyuefei.write.util.ClassUtils;
import org.keyuefei.write.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class ExcelHeadProperty {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelHeadProperty.class);

    private Class headClazz;
    private int headRowNumber;
    private boolean needGroup;
    private List<ExcelHead> heads;
    private List<ExcelHeadGroup> groups;
    private List<List<ExcelMatchKey>> matchKeys;

    private Map<Integer, Field> sortedAllFiledMap;
    private Map<Integer, ExcelContentProperty> contentPropertyMap;

    public ExcelHeadProperty(AbstractWriteHolder holder, Class headClazz) {
        this.headClazz = headClazz;
        sortedAllFiledMap = new TreeMap<Integer, Field>();
        contentPropertyMap = new TreeMap<Integer, ExcelContentProperty>();
        headRowNumber = 0;
        heads = initExcelHeads(holder);
    }


    private List<ExcelHead> initExcelHeads(AbstractWriteHolder holder) {
        if (headClazz == null) {
            return null;
        }
        //暂时无用
        Map<Integer, Field> indexFiledMap = new TreeMap<>();
        Map<String, Field> ignoreMap = new TreeMap<>();

        boolean needIgnore = !CollectionUtils.isEmpty(holder.getExcludeColumnFiledNames()) ||
                !CollectionUtils.isEmpty(holder.getIncludeColumnFiledNames());

        //获取excel类中需要展示的字段-sortedAllFiledMap
        ClassUtils.declaredFields(headClazz, sortedAllFiledMap, indexFiledMap, ignoreMap, needIgnore, holder);

        return initHeadProperty(initHeads());
    }



    private List initHeads() {
        List<List<String>> headNames = new ArrayList<>();
        List<ExcelHeadGroup> groups = new ArrayList<>();

        List<List<ExcelMatchKey>> matchKeys = new ArrayList<>();

        //注解 ExcelMatch， key值、key顺序必须严格一致
        Set<String> matchKeysRule = new HashSet<>(), tmpMatchKeysRule = new HashSet<>();
        for (Map.Entry<Integer, Field> entry : sortedAllFiledMap.entrySet()) {
            Field field = entry.getValue();
            int index = entry.getKey();
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            boolean notForceName = excelProperty == null || excelProperty.value().length <= 0
                    || (excelProperty.value().length == 1 && StringUtils.isEmpty((excelProperty.value())[0]));
            List<String> tmpHeadNames = new ArrayList<>();
            if (notForceName) {
                tmpHeadNames.add(field.getName());
            } else {
                Collections.addAll(tmpHeadNames, excelProperty.value());
            }

            headNames.add(tmpHeadNames);

            ExcelContentProperty excelContentProperty = new ExcelContentProperty();
            if (excelProperty != null) {
                Class<? extends Converter> convertClazz = excelProperty.converter();
                if (convertClazz != AutoConverter.class) {
                    try {
                        Converter converter = convertClazz.newInstance();
                        excelContentProperty.setConverter(converter);
                    } catch (Exception e) {
                        throw new ExcelCommonException("Can not instance custom converter:" + convertClazz.getName());
                    }
                }
            }

            excelContentProperty.setField(field);
            excelContentProperty
                    .setDateTimeFormatProperty(DateTimeFormatProperty.build(field.getAnnotation(DateTimeFormat.class)));
            excelContentProperty
                    .setNumberFormatProperty(NumberFormatProperty.build(field.getAnnotation(NumberFormat.class)));
            contentPropertyMap.put(index, excelContentProperty);


            ExcelGroup excelGroup = field.getAnnotation(ExcelGroup.class);
            if (excelGroup != null) {
                notForceName = StringUtils.isEmpty(excelGroup.key());
                if (notForceName) {
                    groups.add(new ExcelHeadGroup(field.getName()));
                } else {
                    groups.add(new ExcelHeadGroup(excelGroup.key()));
                }
            }

            //match
            ExcelMatch[] matches = field.getAnnotationsByType(ExcelMatch.class);
            if (matches != null && matches.length != 0) {
                List<ExcelMatchKey> tmpMatchKeys = new ArrayList<>();
                for (ExcelMatch match : matches) {
                    tmpMatchKeys.add(new ExcelMatchKey(match.key(), match.value()));
                    matchKeysRule.add(match.key());

                    if (tmpMatchKeysRule.size() != 0 && !tmpMatchKeysRule.contains(match.key())) {
                        throw new ExcelCommonException("属性：" + field.getName() + "上的@ExcelMatch注解key值必须与其他属性key值一致，key：" + match.key());
                    }
                }

                if (tmpMatchKeysRule.size() != 0 && tmpMatchKeysRule.size() != matchKeysRule.size()) {
                    throw new ExcelCommonException("属性：" + field.getName() + "上的@ExcelMatch注解key值必须与其他属性key值一致");
                }

                if (matches.length != matchKeysRule.size()) {
                    throw new ExcelCommonException("属性：" + field.getName() + "上的@ExcelMatch注解key值重复");
                }

                tmpMatchKeysRule = new HashSet<>(matchKeysRule);
                matchKeysRule.clear();

                matchKeys.add(tmpMatchKeys);
            }
        }

        if (LOGGER.isDebugEnabled() && groups.size() > 0) {
            StringBuilder sortedGroup = new StringBuilder("【");
            for (ExcelHeadGroup group : groups) {
                sortedGroup.append(group.getKey() + "、");
            }
            sortedGroup.deleteCharAt(sortedGroup.lastIndexOf("、")).append("】");
            LOGGER.debug("分组顺序：{}", sortedGroup);
        }
        if (groups.size() == 0) {
            this.needGroup = false;
        } else {
            this.needGroup = true;
            this.groups = groups;
        }
        this.matchKeys = matchKeys;
        return headNames;
    }


    private List<ExcelHead> initHeadProperty(List<List> headNames) {
        //前置要求： 1.clazz中的字段的顺序，需要和excel中表头的顺序一致
        //1. 按照一级表头，分组
        //2. 按照一级、二级表头，联合分组
        //3. 叶子表头无需分组

        //1. 确认表头行数
        for (List headName : headNames) {
            if (headName.size() > headRowNumber) {
                headRowNumber = headName.size();
            }
        }

        //2. 少于行数的表头，补充最后一行至与行数相同
        for (List headName : headNames) {
            int lack = headRowNumber - headName.size();
            int last = headName.size() - 1;
            for (int i = 0; i < lack; i++) {
                headName.add(headName.get(last));
            }
        }
        //按照一级表头，分组
        return groupByLevel(headNames);
    }


    public List<ExcelHead> groupByLevel(List<List> levels) {
        List<ExcelHead> excelHeads = new ArrayList<>();
        groupByLevel(levels, excelHeads, 0, 0, null);
        return excelHeads;
    }


    private void groupByLevel(List<List> levels, List<ExcelHead> excelHeads, int startRowIndex, int startColIndex, ExcelHead parentExcelHead) {
        //保证顺序
        Map<String, List<List>> group = new LinkedHashMap<>();
        for (List<String> level : levels) {
            if (0 == level.size()) {
                continue;
            }
            String key = level.get(0);

            if (!group.containsKey(key)) {
                group.put(key, new ArrayList<>());
            }
            level.remove(key);
            group.get(key).add(level);
        }

        int colIndex = startColIndex;
        for (Map.Entry<String, List<List>> entry : group.entrySet()) {
            String headName = entry.getKey();
            List<List> childHeads = entry.getValue();

            int colSpan = childHeads.size();
            int rowSpan = rowSpan(headName, childHeads);

            ExcelHead excelHead = new ExcelHead(headName, startRowIndex, colIndex, rowSpan, colSpan, parentExcelHead);

            excelHeads.add(excelHead);

            groupByLevel(entry.getValue(), excelHeads, startRowIndex + 1, colIndex, excelHead);
            colIndex += colSpan;
        }

    }

    private int rowSpan(String key, List<List> childHeads) {
        //1. 确定行合并值
        int rowSpan = 1;
        List<String> childHead = childHeads.get(0);
        Iterator<String> iterator = childHead.iterator();
        while (iterator.hasNext()) {
            String headName = iterator.next();
            if (!headName.equals(key)) {
                break;
            }
            iterator.remove();
            rowSpan++;
        }
        return rowSpan;
    }


    /**
     * assert
     **/
    private void assertRight(List<ExcelHead> excelHeads) {
        assertExcelHead(excelHeads.get(0), "学校", 0, 0, 3, 1);
        assertExcelHead(excelHeads.get(1), "年级", 0, 1, 3, 1);
        assertExcelHead(excelHeads.get(2), "班级", 0, 2, 3, 1);
        assertExcelHead(excelHeads.get(3), "学生", 0, 3, 3, 1);

        assertExcelHead(excelHeads.get(4), "2018年", 0, 4, 1, 4);
        assertExcelHead(excelHeads.get(5), "上学期", 1, 4, 1, 2);
        assertExcelHead(excelHeads.get(6), "语文", 2, 4, 1, 1);
        assertExcelHead(excelHeads.get(7), "英语", 2, 5, 1, 1);
        assertExcelHead(excelHeads.get(8), "下学期", 1, 6, 1, 2);
        assertExcelHead(excelHeads.get(9), "语文", 2, 6, 1, 1);
        assertExcelHead(excelHeads.get(10), "英语", 2, 7, 1, 1);

        assertExcelHead(excelHeads.get(11), "2019年", 0, 8, 1, 4);
        assertExcelHead(excelHeads.get(12), "上学期", 1, 8, 1, 2);
        assertExcelHead(excelHeads.get(13), "语文", 2, 8, 1, 1);
        assertExcelHead(excelHeads.get(14), "英语", 2, 9, 1, 1);
        assertExcelHead(excelHeads.get(15), "下学期", 1, 10, 1, 2);
        assertExcelHead(excelHeads.get(16), "语文", 2, 10, 1, 1);
        assertExcelHead(excelHeads.get(17), "英语", 2, 11, 1, 1);

    }

    private void assertExcelHead(ExcelHead excelHead, String text, int row, int col, int rowSpan, int colSpan) {
        if (excelHead == null) {
            throw new RuntimeException();
        }

        if (!text.equals(excelHead.getText())) {
            throw new RuntimeException();
        }

        if (row != excelHead.getRow()) {
            throw new RuntimeException();
        }

        if (col != excelHead.getCol()) {
            throw new RuntimeException();
        }

        if (rowSpan != excelHead.getRowSpan()) {
            throw new RuntimeException();
        }

        if (colSpan != excelHead.getColSpan()) {
            throw new RuntimeException();
        }
    }

    /**
     * assert
     **/


    public Class getHeadClazz() {
        return headClazz;
    }

    public void setHeadClazz(Class headClazz) {
        this.headClazz = headClazz;
    }

    public int getHeadRowNumber() {
        return headRowNumber;
    }

    public void setHeadRowNumber(int headRowNumber) {
        this.headRowNumber = headRowNumber;
    }

    public List<ExcelHead> getHeads() {
        return heads;
    }

    public void setHeads(List<ExcelHead> heads) {
        this.heads = heads;
    }

    public Map<Integer, Field> getSortedAllFiledMap() {
        return sortedAllFiledMap;
    }

    public void setSortedAllFiledMap(Map<Integer, Field> sortedAllFiledMap) {
        this.sortedAllFiledMap = sortedAllFiledMap;
    }

    public List<ExcelHeadGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<ExcelHeadGroup> groups) {
        this.groups = groups;
    }

    public List<List<ExcelMatchKey>> getMatchKeys() {
        return matchKeys;
    }

    public void setMatchKeys(List<List<ExcelMatchKey>> matchKeys) {
        this.matchKeys = matchKeys;
    }

    public boolean isNeedGroup() {
        return needGroup;
    }

    public void setNeedGroup(boolean needGroup) {
        this.needGroup = needGroup;
    }

    public Map<Integer, ExcelContentProperty> getContentPropertyMap() {
        return contentPropertyMap;
    }

    public void setContentPropertyMap(Map<Integer, ExcelContentProperty> contentPropertyMap) {
        this.contentPropertyMap = contentPropertyMap;
    }
}
