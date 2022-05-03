package yoonleeverse.onlinecourseback.modules.course.types.input;

import lombok.Data;

@Data
public class VideoInput {
    private String title;
    private Integer time;
    private String link;
    private Boolean freePreview;
    private String text;
}
