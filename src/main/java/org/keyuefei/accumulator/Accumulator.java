package org.keyuefei.accumulator;

import org.keyuefei.model.Excel;

/**
 * @Description 累加器
 * @Author 003654
 * @DATE 2020/6/19
 * @TIME 14:43
 */
public interface Accumulator {

    public int accumulate(int match, Excel excel);
}
