package yoonleeverse.onlinecourseback.modules.course.types.input;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

@Data
public class AddCommentInput {

    private String videoId;
    private String parentId;
    private String content;

    private UserEntity writer;
    private VideoEntity video;
}
