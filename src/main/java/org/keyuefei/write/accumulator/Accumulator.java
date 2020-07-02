package org.keyuefei.write.accumulator;

/**
 * @Description 累加器
 * @Author 003654
 * @DATE 2020/6/19
 * @TIME 14:43
 */
public interface Accumulator<T> {

    public Object accumulate(int rowIndex, int colIndex, T t);

    public Object accumulate(int rowIndex, int colIndex, Object old, T t);
}
