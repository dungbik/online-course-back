package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;
import yoonleeverse.onlinecourseback.modules.course.types.CourseType;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentEntity;

@Data
public class PaymentType {

    private Integer amount;
    private String status;
    private CourseType course;

    public PaymentType(PaymentEntity payment) {
        this.amount = payment.getAmount();
        this.status = payment.getStatus().name();

        if (payment.getCourse() != null)
            this.course = new CourseType(payment.getCourse());
    }
}
