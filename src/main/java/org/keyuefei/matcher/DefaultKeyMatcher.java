package org.keyuefei.matcher;

import org.keyuefei.data.TestData1;
import org.keyuefei.model.HeadKey;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @Description key匹配器
 * @Author 003654
 * @DATE 2020/6/15
 * @TIME 10:31
 */
public class DefaultKeyMatcher implements KeyMatcher {


    @Override
    public boolean match(List<HeadKey> headKeys, TestData1 t) {

        for (HeadKey headKey : headKeys) {
            String key = headKey.getKey();
            Object value = headKey.getValue();
            try {
                Field field = t.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(t);
                if(!value.equals(o)){
                    return false;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }


        }
        return true;
    }
}
