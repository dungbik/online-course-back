package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.TechEntity;

public interface TechRepository extends JpaRepository<TechEntity, Long> {

    boolean existsByName(String name);
}
