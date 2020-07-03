package org.keyuefei.write.metadata;


import lombok.Data;

import java.util.List;

public class BasicParameter {
    private List<List<String>> head;


    private Class clazz;

    public List<List<String>> getHead() {
        return head;
    }

    public void setHead(List<List<String>> head) {
        this.head = head;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
