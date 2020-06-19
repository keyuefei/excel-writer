package org.keyuefei.accumulator;

import org.keyuefei.model.Excel;

/**
 * @Description 累加器
 * @Author 003654
 * @DATE 2020/6/19
 * @TIME 14:43
 */
public class DefaultAccumulator implements Accumulator{

    @Override
    public int accumulate(int match, Excel excel) {
        return 1;
    }
}
