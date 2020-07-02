package org.keyuefei.write.util;

import org.keyuefei.annotation.ExcelIgnore;
import org.keyuefei.annotation.ExcelProperty;
import org.keyuefei.exception.ExcelCommonException;
import org.keyuefei.write.metadata.holder.AbstractWriteHolder;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ClassUtils {

    private static final Map<Class, SoftReference<FieldCache>> FIELD_CACHE = new ConcurrentHashMap<>();

    public static void declaredFields(Class clazz, Map<Integer, Field> sortedAllFiledMap,
                                      Map<Integer, Field> indexFiledMap, Map<String, Field> ignoreMap,
                                      Boolean needIgnore, AbstractWriteHolder holder) {
        FieldCache fieldCache = getFieldCache(clazz);
        if (fieldCache == null) {
            return;
        }
        if (ignoreMap != null) {
            ignoreMap.putAll(fieldCache.getIgnoreMap());
        }
        Map<Integer, Field> tempIndexFildMap = indexFiledMap;
        if (tempIndexFildMap == null) {
            tempIndexFildMap = new TreeMap<>();
        }
        tempIndexFildMap.putAll(fieldCache.getIndexFiledMap());

        //若sheet配置了 excludeColumnFiledNames、 includeColumnFiledNames 则需要再次提出，
        //否则只过滤注解@ExcelIgnore标注的字段
        if (!needIgnore) {
            sortedAllFiledMap.putAll(fieldCache.getSortedAllFiledMap());
            return;
        }

        int index = 0;
        for (Map.Entry<Integer, Field> entry : fieldCache.getSortedAllFiledMap().entrySet()) {
            Field field = entry.getValue();
            if (holder.ignore(entry.getValue().getName())) {
                if (ignoreMap != null) {
                    ignoreMap.put(field.getName(), field);
                }
                while (tempIndexFildMap.containsKey(index)) {
                    tempIndexFildMap.remove(index);
                    index++;
                }
            } else {
                sortedAllFiledMap.put(index, field);
                index++;
            }
        }
    }


    private static FieldCache getFieldCache(Class clazz) {
        if (clazz == null) {
            return null;
        }
        SoftReference<FieldCache> fieldCacheSoftReference = FIELD_CACHE.get(clazz);
        if (fieldCacheSoftReference != null && fieldCacheSoftReference.get() != null) {
            return fieldCacheSoftReference.get();
        }
        synchronized (clazz) {
            fieldCacheSoftReference = FIELD_CACHE.get(clazz);
            if (fieldCacheSoftReference != null && fieldCacheSoftReference.get() != null) {
                return fieldCacheSoftReference.get();
            }
            declaredFields(clazz);
        }
        return FIELD_CACHE.get(clazz).get();
    }

    private static void declaredFields(Class clazz) {
        List<Field> tempFieldList = new ArrayList<>();
        Class tempClass = clazz;
        //支持继承
        while (tempClass != null) {
            Collections.addAll(tempFieldList, tempClass.getDeclaredFields());
            tempClass = tempClass.getSuperclass();
        }
        // Screening of field
        Map<Integer, List<Field>> orderFiledMap = new TreeMap<>();
        Map<Integer, Field> indexFiledMap = new TreeMap<>();
        Map<String, Field> ignoreMap = new HashMap<>(16);

        for (Field field : tempFieldList) {
            declaredOneField(field, orderFiledMap, indexFiledMap, ignoreMap);
        }
        FIELD_CACHE.put(clazz, new SoftReference<>(
                new FieldCache(buildSortedAllFiledMap(orderFiledMap, indexFiledMap), indexFiledMap, ignoreMap)));
    }

    private static Map<Integer, Field> buildSortedAllFiledMap(Map<Integer, List<Field>> orderFiledMap,
                                                              Map<Integer, Field> indexFiledMap) {

        Map<Integer, Field> sortedAllFiledMap = new HashMap<Integer, Field>(
                (orderFiledMap.size() + indexFiledMap.size()) * 4 / 3 + 1);

        Map<Integer, Field> tempIndexFiledMap = new HashMap<Integer, Field>(indexFiledMap);
        int index = 0;
        for (List<Field> fieldList : orderFiledMap.values()) {
            for (Field field : fieldList) {
                while (tempIndexFiledMap.containsKey(index)) {
                    sortedAllFiledMap.put(index, tempIndexFiledMap.get(index));
                    tempIndexFiledMap.remove(index);
                    index++;
                }
                sortedAllFiledMap.put(index, field);
                index++;
            }
        }
        sortedAllFiledMap.putAll(tempIndexFiledMap);
        return sortedAllFiledMap;
    }

    /**
     *
     * @param field
     * @param orderFiledMap
     * @param indexFiledMap
     * @param ignoreMap
     */
    private static void declaredOneField(Field field, Map<Integer, List<Field>> orderFiledMap, Map<Integer, Field> indexFiledMap, Map<String, Field> ignoreMap) {
        ExcelIgnore excelIgnore = field.getAnnotation(ExcelIgnore.class);
        if (excelIgnore != null) {
            ignoreMap.put(field.getName(), field);
            return;
        }
        ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
        boolean noExcelProperty = excelProperty == null;
        if (noExcelProperty) {
            ignoreMap.put(field.getName(), field);
            return;
        }

        if (excelProperty != null && excelProperty.index() >= 0) {
            if (indexFiledMap.containsKey(excelProperty.index())) {
                throw new ExcelCommonException("The index of '" + indexFiledMap.get(excelProperty.index()).getName()
                        + "' and '" + field.getName() + "' must be inconsistent");
            }
            indexFiledMap.put(excelProperty.index(), field);
            return;
        }

        int order = Integer.MAX_VALUE;
        if (excelProperty != null) {
            order = excelProperty.order();
        }
        List<Field> orderFiledList = orderFiledMap.get(order);
        if (orderFiledList == null) {
            orderFiledList = new ArrayList<>();
            orderFiledMap.put(order, orderFiledList);
        }
        orderFiledList.add(field);
    }

    private static class FieldCache {

        private Map<Integer, Field> sortedAllFiledMap;
        private Map<Integer, Field> indexFiledMap;
        private Map<String, Field> ignoreMap;

        public FieldCache(Map<Integer, Field> sortedAllFiledMap, Map<Integer, Field> indexFiledMap,
                          Map<String, Field> ignoreMap) {
            this.sortedAllFiledMap = sortedAllFiledMap;
            this.indexFiledMap = indexFiledMap;
            this.ignoreMap = ignoreMap;
        }

        public Map<Integer, Field> getSortedAllFiledMap() {
            return sortedAllFiledMap;
        }

        public Map<Integer, Field> getIndexFiledMap() {
            return indexFiledMap;
        }

        public Map<String, Field> getIgnoreMap() {
            return ignoreMap;
        }
    }
}
