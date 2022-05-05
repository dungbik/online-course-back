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

    private Long videoId;
    private String title;
    private Integer time;
    private String link;
    private Boolean freePreview;
    private String text;
    private Boolean isCompleted;

//    public VideoType(VideoEntity video) {
//        this.videoId = video.getId();
//        this.title = video.getTitle();
//        this.freePreview = video.getFreePreview();
//    }
//
//    public static VideoType of(VideoEntity video) {
//        return VideoType.builder()
//                .videoId(video.getId())
//                .title(video.getTitle())
//                .time(video.getTime())
//                .link(video.getLink())
//                .freePreview(video.getFreePreview())
//                .text(video.getText())
//                .build();
//    }
}
