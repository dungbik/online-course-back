package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.CommentEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByVideo(VideoEntity video);
}
