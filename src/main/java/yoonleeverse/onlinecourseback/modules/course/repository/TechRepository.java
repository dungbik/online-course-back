package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;

import java.util.List;

public interface TechRepository extends JpaRepository<TechEntity, Long> {

    @Override
    @EntityGraph(attributePaths = {"logo"})
    List<TechEntity> findAll();

    boolean existsByName(String name);

    @Query("SELECT t FROM TechEntity t WHERE t.id IN (:ids)")
    List<TechEntity> findAllByIds(List<Long> ids);
}
