package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoType {

    private String videoId;
    private String title;
    private Integer time;
    private String link;
    private Boolean freePreview;

    public VideoType(VideoEntity video) {
        this.videoId = video.getVideoId();
        this.title = video.getTitle();
        this.freePreview = video.getFreePreview();
    }

    public static VideoType of(VideoEntity video) {
        return VideoType.builder()
                .videoId(video.getVideoId())
                .title(video.getTitle())
                .time(video.getTime())
                .link(video.getLink())
                .freePreview(video.getFreePreview())
                .build();
    }
}
