package org.keyuefei.write.metadata;


import lombok.Data;

import java.util.List;

@Data
public class BasicParameter {
    private List<List<String>> head;


    private Class clazz;
}
