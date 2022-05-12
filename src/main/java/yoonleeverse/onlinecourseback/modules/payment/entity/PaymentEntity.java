package yoonleeverse.onlinecourseback.modules.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.common.entity.BaseTimeEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEnrollmentEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentsDTO;
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

    private String payMethod;

    private String cardName;

    private Integer cancelAmount;

    private String receiptUrl;

    private Long paidAt;

    private String embPgProvider;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "enrollment_id")
    private CourseEnrollmentEntity enrollment;

    public void fail() {
        this.enrollment = null;
        this.status = PaymentStatus.FAIL;
    }

    public void success() {
        this.status = PaymentStatus.SUCCESS;
    }

    public static PaymentEntity makePayment(UserEntity user, CourseEntity course,
                                            CourseEnrollmentEntity enrollment, PaymentsDTO payments) {
        return PaymentEntity.builder()
                .user(user)
                .course(course)
                .merchantUid(Long.parseLong(payments.getMerchantUid()))
                .impUid(Long.parseLong(payments.getImpUid().substring(4)))
                .status(PaymentStatus.PENDING)
                .amount(payments.getAmount())
                .payMethod(payments.getPayMethod())
                .cardName(payments.getCardName())
                .cancelAmount(0)
                .receiptUrl(payments.getReceiptUrl())
                .paidAt(payments.getPaidAt())
                .embPgProvider(payments.getEmbPgProvider())
                .enrollment(enrollment)
                .build();
    }
}
