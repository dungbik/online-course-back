package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.course.types.input.AddCommentInput;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "comments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentId;

    private String content;

    private String parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private VideoEntity video;

    @PrePersist
    void prePersist() {
        this.commentId = UUID.randomUUID().toString();
    }

    public static CommentEntity makeComment(AddCommentInput input) {
        return CommentEntity.builder()
                .content(input.getContent())
                .writer(input.getWriter())
                .video(input.getVideo())
                .build();
    }

    public void updateComment(String content) {
        this.content = content;
    }
}
