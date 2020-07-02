package org.keyuefei.write.accumulator;


import java.lang.reflect.Field;

/**
 * @Description 字段累加器
 * @Author 003654
 * @DATE 2020/6/19
 * @TIME 14:43
 */
public class FieldAccumulator<T> implements Accumulator<T> {

    private String fieldName;

    public FieldAccumulator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public Object accumulate(int rowIndex, int colIndex, T t) {
        return getFieldValue(t);
    }

    @Override
    public Object accumulate(int rowIndex, int colIndex, Object old, T t) {
        return getFieldValue(t);
    }

    private Object getFieldValue(T t) {
        Object value = null;
        try {
            Field field = t.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(t);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }
}
