package org.keyuefei.matcher;

import org.keyuefei.data.TestData1;
import org.keyuefei.model.ExcelHead;
import org.keyuefei.model.ExcelHeadKey;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * @Description key匹配器
 * @Author 003654
 * @DATE 2020/6/15
 * @TIME 10:31
 */
public class DefaultKeyMatcher<T> implements KeyMatcher<T> {

    private static Logger logger = Logger.getLogger(TestData1.class.getName());

    /**
     * 总匹配数
     */
    private int totalMatches;

    private HashMap<List<String>, Integer> keys2colIndexMap;

    private List<String> keys;

    @Override
    public int match(T data) {
        List<String> toMatchKeys = new ArrayList<>(keys.size());
        for (String key : keys) {
            Field field;
            try {
                field = data.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(data);
                toMatchKeys.add(key + ":" + o);
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




    @Override
    public int getTotalMatches() {
        return totalMatches;
    }


    public void init(List<ExcelHead> horizontalLeaves) {
        if (horizontalLeaves == null || horizontalLeaves.size() == 0) {
            throw new RuntimeException("匹配器初始化异常， keys为空");
        }

        keys2colIndexMap = new HashMap(horizontalLeaves.size());
        keys = new ArrayList<>();
        for (int i = 0; i < horizontalLeaves.size(); i++) {
            ExcelHead excelHead = horizontalLeaves.get(i);
            List<String> keys = new ArrayList<>();


            for (ExcelHeadKey headKey : excelHead.getHeadKeys()) {
                keys.add(headKey.getKey() + ":" + headKey.getValue());
            }
            keys2colIndexMap.put(keys, i);
        }

        horizontalLeaves.get(0).getHeadKeys().forEach(excelHeadKey -> {
            keys.add(excelHeadKey.getKey());
        });


    }


}
