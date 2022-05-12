package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentEntity;

@Data
public class PaymentType {

    private Integer amount;
    private String status;
    private CourseType course;
    private String payMethod;
    private String cardName;
    private Integer cancelAmount;
    private String receiptUrl;
    private Long paidAt;
    private String embPgProvider;

    public PaymentType(PaymentEntity payment, CourseType course) {
        this.amount = payment.getAmount();
        this.status = payment.getStatus().name();
        this.course = course;
        this.payMethod = payment.getPayMethod();
        this.cardName = payment.getCardName();
        this.cancelAmount = payment.getCancelAmount();
        this.receiptUrl = payment.getReceiptUrl();
        this.paidAt = payment.getPaidAt();
        this.embPgProvider = payment.getEmbPgProvider();
    }
}
