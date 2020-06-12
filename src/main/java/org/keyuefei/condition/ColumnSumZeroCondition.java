package org.keyuefei.condition;

/**
 * @Description 列和为零 条件
 * @Author 003654
 * @Date 2020/6/12
 * @Time 18:40
 */
public class ColumnSumZeroCondition implements ColumnCondition{


    public boolean invalid() {
        return false;
    }
}