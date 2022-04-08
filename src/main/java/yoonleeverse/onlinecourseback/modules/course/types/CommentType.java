package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.CommentEntity;

@Data
public class CommentType {

    private String commentId;
    private String writer;
    private String content;
    private String parentId;

    public CommentType(CommentEntity comment) {
        this.commentId = comment.getCommentId();
        this.writer = comment.getWriter().getName();
        this.content = comment.getContent();
        this.parentId = comment.getParentId();
    }
}
