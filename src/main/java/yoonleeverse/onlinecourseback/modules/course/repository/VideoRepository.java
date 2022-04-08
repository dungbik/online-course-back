package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoCategoryEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;

import java.util.List;

public interface VideoRepository extends JpaRepository<VideoEntity, Long> {

    void deleteAllByCategoryIn(List<VideoCategoryEntity> categories);
}
