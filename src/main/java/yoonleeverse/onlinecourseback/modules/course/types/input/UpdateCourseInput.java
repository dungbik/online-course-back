package yoonleeverse.onlinecourseback.modules.course.types.input;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCourseInput {
    private String slug;
    private String title;
    private String subTitle;
    private String logo;
    private String mainColor;
    private String level;
    private Integer price;
    private List<Long> mainTechs;
    private List<String> prerequisite;
    private List<CategoryInput> videoCategories;

}
