package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoCategoryEntity;

import java.util.List;

public interface VideoCategoryRepository extends JpaRepository<VideoCategoryEntity, Long> {

    @Query("SELECT vc FROM VideoCategoryEntity vc " +
            "JOIN FETCH vc.course " +
            "JOIN FETCH vc.videos " +
            "WHERE vc.course.courseId IN (:courseIds)")
    List<VideoCategoryEntity> findAllByCourseIdIn(List<String> courseIds);

    @Query("SELECT vc FROM VideoCategoryEntity vc " +
            "JOIN vc.course " +
            "WHERE vc.course.courseId = :courseId")
    List<VideoCategoryEntity> findAllByCourseId(String courseId);

    void deleteAllByCourse(CourseEntity course);
}
