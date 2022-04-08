package yoonleeverse.onlinecourseback.modules.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.CommentEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.VideoEntity;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    List<CommentEntity> findAllByVideo(VideoEntity video);

    Optional<CommentEntity> findByCommentId(String commentId);
}
