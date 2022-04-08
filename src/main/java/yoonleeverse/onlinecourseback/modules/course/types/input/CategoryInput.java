package yoonleeverse.onlinecourseback.modules.course.types.input;

import lombok.Data;

import java.util.List;

@Data
public class CategoryInput {
    private String title;
    private List<VideoInput> videos;
}
