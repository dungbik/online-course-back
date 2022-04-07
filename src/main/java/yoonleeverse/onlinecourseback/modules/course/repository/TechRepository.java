package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;

import java.util.List;

public interface TechRepository extends JpaRepository<TechEntity, Long> {

    boolean existsByName(String name);

    @Query("SELECT t FROM TechEntity t WHERE t.id IN (:ids)")
    List<TechEntity> findAllByIds(List<Long> ids);
}
