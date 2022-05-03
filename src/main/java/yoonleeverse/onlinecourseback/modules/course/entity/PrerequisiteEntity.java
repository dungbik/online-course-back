package yoonleeverse.onlinecourseback.modules.course.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Table(name = "Prerequisites")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrerequisiteEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "required_id")
    private CourseEntity requiredCourse;

    public void setParent(CourseEntity course) {
        this.course = course;
    }
}
