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
            "WHERE vc.course.slug IN (:slugs)")
    List<VideoCategoryEntity> findAllBySlugIn(List<String> slugs);

    @Query("SELECT vc FROM VideoCategoryEntity vc " +
            "JOIN vc.course " +
            "WHERE vc.course.slug = :slug")
    List<VideoCategoryEntity> findAllBySlug(String slug);

    void deleteAllByCourse(CourseEntity course);
}
