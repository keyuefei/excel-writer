package org.keyuefei.matcher;

import org.keyuefei.data.TestData1;
import org.keyuefei.model.ExcelHeadKey;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Logger;

/**
 * @Description key匹配器
 * @Author 003654
 * @DATE 2020/6/15
 * @TIME 10:31
 */
public class DefaultKeyMatcher implements KeyMatcher {

    private static Logger logger = Logger.getLogger(TestData1.class.getName());

    /**
     * 总匹配数
     */
    private int totalMatches;

    @Override
    public boolean match(List<ExcelHeadKey> headKeys, TestData1 t) {

        StringBuilder logStr = new StringBuilder();

        for (ExcelHeadKey headKey : headKeys) {
            String key = headKey.getKey();
            Object value = headKey.getValue();
            try {
                Field field = t.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(t);
                if (!value.equals(o)) {
                    return false;
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
            logStr.append(key + ":" + value).append(" ");
        }
        logger.info("数据匹配：" + logStr);
        totalMatches++;
        return true;
    }


    @Override
    public int getTotalMatches() {
        return totalMatches;
    }
}
