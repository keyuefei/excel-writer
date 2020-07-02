package org.keyuefei.write.accumulator;


/**
 * @Description 累加器
 * @Author 003654
 * @DATE 2020/6/19
 * @TIME 14:43
 */
public class DefaultAccumulator<T> implements Accumulator<T> {

    @Override
    public Object accumulate(int rowIndex, int colIndex, T t) {
        return 1;
    }

    @Override
    public Object accumulate(int rowIndex, int colIndex, Object old, T t) {
        return 1;
    }
}
