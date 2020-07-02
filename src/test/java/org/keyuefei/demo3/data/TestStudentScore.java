package org.keyuefei.demo3.data;

import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @Description testData1
 * @Author 003654
 * @Date 2020/6/15
 * @Time 9:45
 */
public class TestStudentScore {

    private static Logger logger = Logger.getLogger(TestStudentScore.class.getName());


    private String school;

    private String grade;

    private String className;

    private String studentName;


    //年份
    private String year;

    //学期
    private String semester;

    //课程
    private String course;

    private Double score;


    public static List<TestStudentScore> buildFakeData() {
        List<TestStudentScore> fakeData = new ArrayList<>();
        String[] schools = new String[]{"清华", "北大", "上交"};
        String[] grades = new String[]{"一年级", "二年级", "三年级"};
        //todo 若数据库查询出来的是"语文", "英语"；而key是"chinese", "english"；需要加上别名。
        String[] courses = new String[]{"chinese", "english"};
        String[] years = new String[]{"2018", "2019"};
        String[] semesters = new String[]{"last", "next"};
        Map<String, String[]> classNames = new HashMap(16);
        classNames.put("清华-一年级", new String[]{"一班", "二班"});
        classNames.put("清华-二年级", new String[]{"一班", "二班"});
        classNames.put("清华-三年级", new String[]{"一班", "二班"});

        classNames.put("北大-一年级", new String[]{"三十三班", "三十四班"});
        classNames.put("北大-二年级", new String[]{"三十三班", "三十四班"});
        classNames.put("北大-三年级", new String[]{"三十三班", "三十四班"});

        classNames.put("上交-一年级", new String[]{"九班", "十班"});
        classNames.put("上交-二年级", new String[]{"九班", "十班"});
        classNames.put("上交-三年级", new String[]{"九班", "十班"});

        String[] students = new String[]{"李华", "小明", "基德", "柯南", "柯北", "刘德华"};

        int fakeDataCount = 10;
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
            String school = schools[RandomUtils.nextInt(0, schools.length)];
            String grade = grades[RandomUtils.nextInt(0, grades.length)];
            String course = courses[RandomUtils.nextInt(0, courses.length)];
            String student = students[RandomUtils.nextInt(0, students.length)];
            String year = years[RandomUtils.nextInt(0, years.length)];
            String semester = semesters[RandomUtils.nextInt(0, semesters.length)];
            String[] classNameArray = classNames.get(school + "-" + grade);
            String className = classNameArray[RandomUtils.nextInt(0, classNameArray.length)];
            TestStudentScore studentScore = new TestStudentScore();

            studentScore.setSchool(school);
            studentScore.setGrade(grade);
            studentScore.setClassName(className);

            studentScore.setCourse(course);
            studentScore.setStudentName(student);


            studentScore.setYear(year);
            studentScore.setSemester(semester);
            studentScore.setScore(RandomUtils.nextDouble(50.0, 100.0));



            sum++;
            fakeData.add(studentScore);
        }
        logger.info("总数:" + sum);
        return fakeData;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}