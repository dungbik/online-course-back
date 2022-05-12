package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    boolean existsByTitle(String title);

    List<CourseEntity> getAllBySlugIn(List<String> slugs);

    Optional<CourseEntity> findBySlug(String slug);
}
