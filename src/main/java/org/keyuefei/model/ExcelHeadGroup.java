package org.keyuefei.model;

import lombok.Data;

import java.util.Comparator;

/**
 * @Description 表头分组节点
 * @Author 003654
 * @Date 2020/6/16
 * @Time 18:35
 */
@Data
public class ExcelHeadGroup {


    private String key;

    public ExcelHeadGroup(String key) {
        this.key = key;
    }

}