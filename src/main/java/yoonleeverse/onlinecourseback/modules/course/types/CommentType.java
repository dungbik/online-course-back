package yoonleeverse.onlinecourseback.modules.course.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.entity.CommentEntity;

@Data
public class CommentType {

    private Long id;
    private String writer;
    private String content;
    private String parentId;

    public CommentType(CommentEntity comment) {
        this.id = comment.getId();
        this.writer = comment.getWriter().getName();
        this.content = comment.getContent();
        this.parentId = comment.getParentId();
    }
}
