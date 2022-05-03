package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

@Data
public class CourseType {

    private String slug;
    private String title;
    private String subTitle;
    private String logo;
    private String mainColor;
    private String level;
    private Integer price;

    public CourseType(CourseEntity course, String cloudUrl) {
        this.slug = course.getSlug();
        this.title = course.getTitle();
        this.subTitle = course.getSubTitle();
        
        FileEntity logo = course.getLogo();
        if (logo != null) {
            this.logo = String.format("%s/%s", cloudUrl, logo.getFileUrl());
        }
        this.mainColor = course.getMainColor();
        this.level = course.getLevel().getName();
        this.price = course.getPrice();
    }
}
