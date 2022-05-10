package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class PaymentsDTO {

    private String impUid;
    private String merchantUid;
    private String payMethod;
    private String channel;

    private String pgProvider;
    private String embPgProvider;

    private String pgTid;
    private String pgId;

    private Boolean escrow;
    private String applyNum;

    private String bankCode;
    private String bankName;

    private String cardCode;
    private String cardName;
    private Integer cardQuota;
    private String cardNumber;
    private String cardType;

    private String vbankCode;
    private String vbankName;
    private String vbankNum;
    private String vbankHolder;
    private Long vbankDate;
    private Long vbankIssuedAt;

    private String name;
    private Integer amount;
    private Integer cancelAmount;
    private String currency;

    private String buyerName;
    private String buyerEmail;
    private String buyerTel;
    private String buyerAddr;
    private String buyerPostcode;

    private String customData;
    private String userAgent;
    private String status;

    private Long startedAt;
    private Long paidAt;
    private Long failedAt;
    private Long cancelledAt;

    private String failReason;
    private String cancelReason;

    private String receiptUrl;
    private List<CancelHistoryDTO> cancelHistory;
    private List<String> cancelReceiptUrls;
    private Boolean cashReceiptIssued;

    private String customerUid;
    private String customerUidUsage;
}
