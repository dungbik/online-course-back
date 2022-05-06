package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEnrollmentEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollmentEntity, Long> {

    Optional<CourseEnrollmentEntity> findByUserAndCourse(UserEntity user, CourseEntity course);

    @Query("SELECT ce.course.slug FROM CourseEnrollmentEntity ce WHERE ce.user = :user")
    List<String> findCourseIds(UserEntity user);

    @Query("SELECT ce.course.slug FROM CourseEnrollmentEntity ce WHERE ce.user = :user AND ce.course = :course")
    List<String> findCourseIdsByCourse(UserEntity user, CourseEntity course);
}
