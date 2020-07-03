package org.keyuefei.write.metadata.holder;


import org.keyuefei.write.converters.Converter;
import org.keyuefei.write.metadata.BasicParameter;

import java.util.List;
import java.util.Map;

public abstract class AbstractHolder {
    private List<List<String>> head;

    private Class clazz;

    private Map<String, Converter> converterMap;


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


    public Map<String, Converter> converterMap() {
        return getConverterMap();
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

    public Map<String, Converter> getConverterMap() {
        return converterMap;
    }

    public void setConverterMap(Map<String, Converter> converterMap) {
        this.converterMap = converterMap;
    }
}
