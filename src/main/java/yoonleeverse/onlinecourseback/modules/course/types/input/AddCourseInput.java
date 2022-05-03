package yoonleeverse.onlinecourseback.modules.course.types.input;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;

import java.util.List;

@Data
public class AddCourseInput {
    private String title;
    private String subTitle;
    private String mainColor;
    private String level;
    private Integer price;
    private List<Long> mainTechs;
    private List<String> prerequisite;
    private List<CategoryInput> videoCategories;

    private FileEntity logo;
}
