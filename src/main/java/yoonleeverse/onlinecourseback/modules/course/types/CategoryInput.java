package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;

import java.util.List;

@Data
public class CategoryInput {
    private String title;
    private List<VideoInput> videos;
}
