package org.keyuefei.data;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

/**
 * @Description testData1
 * @Author 003654
 * @Date 2020/6/15
 * @Time 9:45
 */
@Data
public class TestData1 {

    private String cubicleType;

    private String figureCode;

    private String roomRelation;

    private String supplierName;

    private String boxCityName;

    private String region;


    public static List<TestData1> buildFakeData() {
        List<TestData1> fakeData = new ArrayList<>();
        String[] cubicleTypes = new String[]{"expressMainBox", "expressSubBox", "expressIIIColumnSubBox", "simpleVersionMainBox", "simpleVersionIIIColumnSubBox",
                "ezExpressMainBox", "ezExpressSubBox", "depositFiveMainBox", "depositFiveSubBox", "mergeMainBox", "mergeSubBox",
                "letterMainBox", "letterSubBox", "exLetterMainBox", "exLetterSubBox"};
        String[] supplierNames = new String[]{"柏顺星", "登丰", "东城", "格兰达", "派宝", "鹏科", "祁飞", "盛海", "速易宝", "网盒", "新北洋", "智莱", "智能", "中浩"};
        String[] boxCityNames = new String[]{"武汉", "深圳", "北京", "上海", "天津", "河北", "山西"};
        Map<String, String> regions = new HashMap(16);
        regions.put("武汉", "华南");
        regions.put("深圳", "华南");
        regions.put("上海", "华南");
        regions.put("北京", "北京");
        regions.put("天津", "华北");
        regions.put("河北", "华北");
        regions.put("山西", "华北");


        int fakeDataCount = 50;
        int sum = 0;
        //随机某一列不没有数据
/*        String displayCubicleType = cubicleTypes[RandomUtils.nextInt(0, cubicleTypes.length)];
        String displayFigureCode = "";
        for (TestData1 boxCubicleTypeHandler : boxCubicleTypeHandlers) {
            WareHouseAssetStockDetailDto detailDto = new WareHouseAssetStockDetailDto();
            detailDto.setCubicleType(displayCubicleType);
            if (boxCubicleTypeHandler.support(detailDto)) {
                String[] figureCodes = boxCubicleTypeHandler.getFigure(displayCubicleType);
                displayFigureCode = figureCodes[RandomUtils.nextInt(figureCodes.length)];
            }
        }*/
//        String displayRoomRelation = RandomUtils.nextInt(2) + "";
//        log.info("不展示的列{}，{}，{}", displayCubicleType, displayRoomRelation, displayFigureCode);
        for (int i = 0; i < fakeDataCount; i++) {
            String cubicleType = cubicleTypes[RandomUtils.nextInt(0, cubicleTypes.length)];

            String[] figureCodes = getFigureCodes(cubicleType);
            if (figureCodes == null) {
                continue;
            }
            String figureCode = figureCodes[RandomUtils.nextInt(0, figureCodes.length)];
            String roomRelation = RandomUtils.nextInt(1, 3) + "";

            TestData1 testData1 = new TestData1();
            testData1.setCubicleType(cubicleType);
            testData1.setFigureCode(figureCode);
            testData1.setRoomRelation(roomRelation);
            testData1.setSupplierName(supplierNames[RandomUtils.nextInt(0, supplierNames.length)]);
            String boxCityName = boxCityNames[RandomUtils.nextInt(0, boxCityNames.length)];
            testData1.setBoxCityName(boxCityName);
            testData1.setRegion(regions.get(boxCityName));
//            if (cubicleType.equals(displayCubicleType) && roomRelation.equals(displayRoomRelation) && figureCode.equals(displayFigureCode)) {
//                continue;
//            }
            sum++;
            fakeData.add(testData1);
        }
//        log.info("总数：{}", sum);
        return fakeData;
    }


    public static String[] getFigureCodes(String cubicleType) {
        if ("expressMainBox".equals(cubicleType)) {
            return new String[]{"0"};
        }
        if ("expressSubBox".equals(cubicleType)) {
            return new String[]{"3", "1", "2"};
        }
        if ("expressIIIColumnSubBox".equals(cubicleType)) {
            return new String[]{"1", "2"};
        }
        return null;
    }


}