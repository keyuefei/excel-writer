package org.keyuefei.write.metadata.head;

public class ExcelMatchKey {
    public ExcelMatchKey(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public ExcelMatchKey() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
