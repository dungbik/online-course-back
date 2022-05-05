package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoHistoryEntity;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface VideoHistoryRepository extends JpaRepository<VideoHistoryEntity, Long> {

    Optional<VideoHistoryEntity> findByUserAndVideo(UserEntity user, VideoEntity video);

    @Query("SELECT vh.video.id FROM VideoHistoryEntity vh " +
            "JOIN vh.video.course " +
            "WHERE vh.user = :user AND vh.video.course = :course")
    List<Long> findVideoIdsByCourse(UserEntity user, CourseEntity course);

    @Query("SELECT vh.video.id FROM VideoHistoryEntity vh " +
            "JOIN vh.video.course " +
            "WHERE vh.user = :user")
    List<Long> findVideoIds(UserEntity user);
}
