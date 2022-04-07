package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    boolean existsByTitle(String title);
}
