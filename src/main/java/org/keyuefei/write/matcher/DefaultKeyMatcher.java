package org.keyuefei.write.matcher;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static org.keyuefei.write.constant.Constant.SEPARATOR;

/**
 * @Description key匹配器
 * @Author 003654
 * @DATE 2020/6/15
 * @TIME 10:31
 */
public class DefaultKeyMatcher implements KeyMatcher {

    private static Logger LOGGER = Logger.getLogger(DefaultKeyMatcher.class.getName());


    private Map<List<String>, Integer> keys2colIndexMap;

    private Set<String> keys;

    public DefaultKeyMatcher(Map<List<String>, Integer> keys2colIndexMap, Set<String> keys) {
        this.keys2colIndexMap = keys2colIndexMap;
        this.keys = keys;
    }

    @Override
    public int match(Object data) {
        List<String> toMatchKeys = new ArrayList<>(keys.size());
        for (String key : keys) {
            Field field;
            try {
                field = data.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(data);
                toMatchKeys.add(key + SEPARATOR + o);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return -1;
            }
        }
        Integer colIndex = keys2colIndexMap.get(toMatchKeys);
        if (colIndex == null) {
            throw new RuntimeException(toMatchKeys.toString() + "没有匹配的数据");
        }
        return colIndex;
    }


}
