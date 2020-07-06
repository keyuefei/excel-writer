package org.keyuefei.demo1;

import org.keyuefei.demo1.annotation.StudentScore;
import org.keyuefei.demo1.data.TestStudentScore;
import org.keyuefei.write.DynamicExcel;
import org.keyuefei.write.accumulator.FieldAccumulator;

/**
 * @Description 测试案例1
 * @Author 003654
 * @Date 2020/6/19
 * @Time 16:45
 */
public class Demo1 {

    public static void main(String[] args) {
        String fileName = org.keyuefei.demo1.Demo1.class.getResource("/").getPath() + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
        //1. 构造假数据
        DynamicExcel.write(fileName, StudentScore.class)
                .sheet(0, "test")
                .accumulator(new FieldAccumulator<>("score"))
                .doWrite(TestStudentScore.buildFakeData());

    }
}