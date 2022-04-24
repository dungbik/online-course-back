package yoonleeverse.onlinecourseback.modules.payment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentEntity;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByMerchantUid(Long merchantUid);
    Optional<PaymentEntity> findByUserAndCourse(UserEntity user, CourseEntity course);

    @EntityGraph(attributePaths = {"course"})
    List<PaymentEntity> findAllByUser(UserEntity user);
}
