package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VideoCategoryType {

    private Long id;
    private String title;
    private List<VideoType> videos;
}
