package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "video_categories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoCategoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VideoEntity> videos = new HashSet<>();

    @Column(unique = true, nullable = false)
    private String categoryId;

    @Column(nullable = false)
    private String title;

    @PrePersist
    void prePersist() {
        this.categoryId = UUID.randomUUID().toString();
    }

    public void setParent(CourseEntity course) {
        this.course = course;
    }
}
