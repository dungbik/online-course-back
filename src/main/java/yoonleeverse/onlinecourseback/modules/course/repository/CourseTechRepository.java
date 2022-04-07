package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseTechEntity;

import java.util.List;

public interface CourseTechRepository extends JpaRepository<CourseTechEntity, Long> {

    @Query("SELECT ct FROM CourseTechEntity ct JOIN FETCH ct.tech JOIN FETCH ct.course WHERE ct.course.courseId = :courseId")
    List<CourseTechEntity> findByCourseId(String courseId);
}
