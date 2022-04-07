package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoCategoryEntity;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class VideoCategoryType {

    private String categoryId;
    private String title;
    private List<VideoType> videos;

    public VideoCategoryType(VideoCategoryEntity videoCategory) {
        this.categoryId = videoCategory.getCategoryId();
        this.title = videoCategory.getTitle();
        this.videos = videoCategory.getVideos().stream()
                .map(VideoType::new)
                .collect(Collectors.toList());
    }
}
