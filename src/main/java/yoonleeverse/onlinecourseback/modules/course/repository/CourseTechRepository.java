package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseTechEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;

import java.util.List;

public interface CourseTechRepository extends JpaRepository<CourseTechEntity, Long> {

    @Query("SELECT ct.course.slug, ct.tech FROM CourseTechEntity ct " +
            "JOIN ct.tech " +
            "JOIN ct.course " +
            "WHERE ct.course.slug IN (:slugs)")
    List<Object[]> findAllBySlugIn(List<String> slugs);

    void deleteAllByTech(TechEntity tech);

    void deleteAllByCourse(CourseEntity course);
}
