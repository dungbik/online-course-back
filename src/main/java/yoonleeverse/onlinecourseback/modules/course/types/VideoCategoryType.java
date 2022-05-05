package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Builder;
import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoCategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class VideoCategoryType {

    private String categoryId;
    private String title;
    private List<VideoType> videos;
}
