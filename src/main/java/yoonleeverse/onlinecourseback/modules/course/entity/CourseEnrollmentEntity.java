package yoonleeverse.onlinecourseback.modules.course.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "courseEnrollment", uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "course_id"})
})
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private CourseEntity course;
}
