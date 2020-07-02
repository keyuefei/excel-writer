package org.keyuefei.write.metadata.head;

/**
 * @Description 表头分组节点
 * @Author 003654
 * @Date 2020/6/16
 * @Time 18:35
 */
public class ExcelHeadGroup {

    private String key;

    public ExcelHeadGroup(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}