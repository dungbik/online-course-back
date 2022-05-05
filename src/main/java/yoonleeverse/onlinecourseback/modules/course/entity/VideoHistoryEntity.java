package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "videoHistory", uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "video_id"})
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private VideoEntity video;
}
