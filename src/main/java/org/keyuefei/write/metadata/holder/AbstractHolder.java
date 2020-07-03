package org.keyuefei.write.metadata.holder;


import org.keyuefei.write.metadata.BasicParameter;

import java.util.List;
import java.util.Locale;

public abstract class AbstractHolder {
    private List<List<String>> head;
    private Class clazz;

    public AbstractHolder(BasicParameter basicParameter, AbstractHolder prentAbstractHolder) {
        if (basicParameter.getHead() == null && basicParameter.getClazz() == null && prentAbstractHolder != null) {
            this.head = prentAbstractHolder.getHead();
        } else {
            this.head = basicParameter.getHead();
        }
        if (basicParameter.getHead() == null && basicParameter.getClazz() == null && prentAbstractHolder != null) {
            this.clazz = prentAbstractHolder.getClazz();
        } else {
            this.clazz = basicParameter.getClazz();
        }

    }


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
