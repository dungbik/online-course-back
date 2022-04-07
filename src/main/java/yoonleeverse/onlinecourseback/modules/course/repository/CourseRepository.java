package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;

import java.util.List;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    boolean existsByTitle(String title);

     List<CourseEntity> getAllByCourseIdIn(List<String> courseId);

     CourseEntity findByCourseId(String courseId);
}
