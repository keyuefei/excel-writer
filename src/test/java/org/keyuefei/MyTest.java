package org.keyuefei;

import org.junit.Test;
import org.keyuefei.data.TestData1;
import org.keyuefei.exception.GroupFieldException;
import org.keyuefei.model.ExcelHead;

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
        Main.group(levelExcelHeads, data, new String[]{""}, 0);
        System.out.println(levelExcelHeads);
    }

    @Test
    public void testGroup_notGroupField1() throws GroupFieldException {
        //1. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //2.
        Map<Integer, List<ExcelHead>> levelExcelHeads = new HashMap<>();
        Main.group(levelExcelHeads, data, new String[]{}, 0);
        System.out.println(levelExcelHeads);
    }


    @Test
    public void testGroup() throws GroupFieldException {
        //1. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        //2.
        Map<Integer, List<ExcelHead>> levelExcelHeads = new HashMap<>();
        Main.group(levelExcelHeads, data, new String[]{"region","boxCityName", "supplierName"}, 0);
        System.out.println(levelExcelHeads);
    }

}