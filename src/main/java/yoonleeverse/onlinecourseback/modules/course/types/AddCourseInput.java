package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;

import java.util.List;

@Data
public class AddCourseInput {
    private String title;
    private String subTitle;
    private String logo;
    private String mainColor;
    private String level;
    private Integer price;
    private List<Long> mainTechs;

}
