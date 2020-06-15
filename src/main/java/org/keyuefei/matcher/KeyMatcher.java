package org.keyuefei.matcher;/**
 * @Classname KeyMatcher
 * @Description TODO
 * @Date 2020/6/15 10:31
 * @Created by keyuefei
 * @author KeyMatcher 1063847690@qq.com
 */

import org.keyuefei.data.TestData1;

/**
 * @Description key匹配器
 * @Author 003654
 * @DATE 2020/6/15
 * @TIME 10:31
 */
public interface KeyMatcher {

    boolean match(String key, TestData1 t);
}
