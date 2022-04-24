package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;

@Data
public class PaymentInput {

    private String courseId;
    private String impUid; // 결제 번호
    private Long merchantUid; // 주문 번호
}
