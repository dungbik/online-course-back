package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;

@Data
public class CancelHistoryDTO {
    private String pgTid;
    private Integer amount;
    private Long cancelledAt;
    private String reason;
    private String receiptUrl;
}
