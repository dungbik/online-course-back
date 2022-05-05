package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoHistoryEntity;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import java.util.Optional;

public interface VideoHistoryRepository extends JpaRepository<VideoHistoryEntity, Long> {

    Optional<VideoHistoryEntity> findByUserAndVideo(UserEntity user, VideoEntity video);
}
