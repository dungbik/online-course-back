package yoonleeverse.onlinecourseback.modules.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentInput;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import javax.persistence.*;

@Entity
@Table(name = "payments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Long impUid;

    private Long merchantUid;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private Integer amount;

    public void cancel() {
        this.status = PaymentStatus.FAIL;
    }

    public void success() {
        this.status = PaymentStatus.SUCCESS;
    }

    public static PaymentEntity makePayment(UserEntity user, CourseEntity course, PaymentInput input) {
        return PaymentEntity.builder()
                .user(user)
                .course(course)
                .merchantUid(input.getMerchantUid())
                .impUid(Long.parseLong(input.getImpUid().substring(4)))
                .status(PaymentStatus.PENDING)
                .amount(input.getAmount())
                .build();
    }
}
