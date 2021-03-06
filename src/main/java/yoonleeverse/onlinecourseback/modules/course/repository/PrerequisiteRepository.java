package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.PrerequisiteEntity;

import java.util.List;

public interface PrerequisiteRepository extends JpaRepository<PrerequisiteEntity, Long> {

    @Query("SELECT p.course.slug, p.requiredCourse FROM PrerequisiteEntity p " +
            "JOIN p.course " +
            "JOIN p.requiredCourse " +
            "WHERE p.course.slug IN (:slugs)")
    List<Object[]> findAllBySlugIn(List<String> slugs);

    void deleteAllByCourse(CourseEntity course);
}
