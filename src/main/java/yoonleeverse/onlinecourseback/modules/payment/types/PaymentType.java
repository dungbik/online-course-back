package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentEntity;

@Data
public class PaymentType {

    private Integer amount;
    private String status;
    private CourseType course;

    public PaymentType(PaymentEntity payment, CourseType course) {
        this.amount = payment.getAmount();
        this.status = payment.getStatus().name();
        this.course = course;
    }
}
