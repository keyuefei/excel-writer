package org.keyuefei.write.metadata;


import org.keyuefei.write.converters.Converter;

import java.util.List;

public class BasicParameter {
    private List<List<String>> head;


    private Class clazz;

    /**
     * Custom type conversions override the default
     */
    private List<Converter> customConverterList;


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

    public List<Converter> getCustomConverterList() {
        return customConverterList;
    }

    public void setCustomConverterList(List<Converter> customConverterList) {
        this.customConverterList = customConverterList;
    }
}
