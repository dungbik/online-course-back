package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;

@Data
public class CourseType {

    private String courseId;
    private String title;
    private String subTitle;
    private String logo;
    private String mainColor;
    private String level;
    private Integer price;

    public CourseType(CourseEntity course) {
        this.courseId = course.getCourseId();
        this.title = course.getTitle();
        this.subTitle = course.getSubTitle();
        this.logo = course.getLogo();
        this.mainColor = course.getMainColor();
        this.level = course.getLevel().getName();
        this.price = course.getPrice();
    }
}
