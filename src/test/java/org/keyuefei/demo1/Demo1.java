package org.keyuefei.demo1;

import org.keyuefei.ExcelFacade;
import org.keyuefei.data.TestData1;
import org.keyuefei.demo1.annotation.BoxCubicle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Description 测试案例1
 * @Author 003654
 * @Date 2020/6/19
 * @Time 16:45
 */
public class Demo1 {
    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {

        //1. 构造假数据
        List<TestData1> data = TestData1.buildFakeData();
        ExcelFacade excelFacade = new ExcelFacade();
        byte[] bytes = excelFacade.export(data, BoxCubicle.class);

        if (bytes != null) {
            String path = "D:\\keyuefei\\project\\dynamic-excel\\src\\main\\java\\org\\keyuefei\\test.xlsx";
            Files.write(Paths.get(path), bytes);
        }


    }
}