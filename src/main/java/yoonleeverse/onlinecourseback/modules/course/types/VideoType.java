package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;

@Data
public class VideoType {

    private String videoId;
    private String title;
    private Integer time;
    private String link;

    public VideoType(VideoEntity video) {
        this.videoId = video.getVideoId();
        this.title = video.getTitle();
        this.time = video.getTime();
        this.link = video.getLink();
    }
}
