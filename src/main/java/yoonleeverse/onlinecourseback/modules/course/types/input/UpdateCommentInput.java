package yoonleeverse.onlinecourseback.modules.course.types.input;

import lombok.Data;

@Data
public class UpdateCommentInput {

    private String commentId;
    private String content;
}
