package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseType {

    private String slug;
    private String title;
    private String subTitle;
    private String logo;
    private String mainColor;
    private String level;
    private Integer price;
}
