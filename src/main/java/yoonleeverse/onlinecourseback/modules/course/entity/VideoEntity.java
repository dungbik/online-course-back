package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "videos")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private VideoCategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Column(unique = true, nullable = false)
    private String videoId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer time;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private Boolean freePreview;

    private String text;

    @PrePersist
    void prePersist() {
        this.videoId = UUID.randomUUID().toString();
    }

    public void setParent(VideoCategoryEntity category, CourseEntity course) {
        this.category = category;
        this.course = course;
    }
}
