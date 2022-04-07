package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "video_categories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCategoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @OneToMany(mappedBy = "category")
    private List<VideoEntity> videos = new ArrayList<>();

    @Column(unique = true, nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String title;

    @PrePersist
    void prePersist() {
        this.categoryId = UUID.randomUUID().toString();
    }
}
