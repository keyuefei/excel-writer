package org.keyuefei;

import org.junit.Assert;
import org.junit.Test;
import org.keyuefei.data.TestData1;
import org.keyuefei.exception.GroupFieldException;
import org.keyuefei.model.ExcelHead;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author 003654
 * @Date 2020/6/15
 * @Time 13:44
 */
public class MyTest {


    @Test
    public void testGroup_notGroupField() throws GroupFieldException {
        //1. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //2.
        Map<Integer, List<ExcelHead>> levelExcelHeads = new HashMap<>();
//        Main.group2Horizontal(levelExcelHeads, data, null, 0);
        System.out.println(levelExcelHeads);
    }

    @Test
    public void testGroup_notGroupField1() throws GroupFieldException {
        //1. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //2.
        Map<Integer, List<ExcelHead>> levelExcelHeads = new HashMap<>();
//        Main.group2Horizontal(levelExcelHeads, data, null, 0);
        System.out.println(levelExcelHeads);
    }


    @Test
    public void testGroup() throws GroupFieldException {
        //1. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //2.
        Map<Integer, List<ExcelHead>> levelExcelHeads = new HashMap<>();
//        Main.group2Horizontal(levelExcelHeads, data, null, 0);
        System.out.println(levelExcelHeads);
    }


    @Test
    public void testKey() {
        HashMap<List<String>, Integer> keys2colIndexMap = new HashMap(4);
        keys2colIndexMap.put(Arrays.asList(new String[]{"fruit:apple", "color:red"}), 1);
        keys2colIndexMap.put(Arrays.asList(new String[]{"fruit:banana", "color:yellow"}), 2);
        //list中顺序一致才可以做为key
        int index = keys2colIndexMap.get(Arrays.asList(new String[]{"fruit:banana", "color:yellow"}));
        Assert.assertEquals(index, 2);
        //不一致时，找不到
        Integer index1 = keys2colIndexMap.get(Arrays.asList(new String[]{"color:yellow", "fruit:banana"}));
        Assert.assertEquals(index1, new Integer(2));
    }

}