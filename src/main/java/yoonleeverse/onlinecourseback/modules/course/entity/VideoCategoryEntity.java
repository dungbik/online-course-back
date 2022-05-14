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
    private List<VideoEntity> videos = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    public void setParent(CourseEntity course) {
        this.course = course;
    }
}
