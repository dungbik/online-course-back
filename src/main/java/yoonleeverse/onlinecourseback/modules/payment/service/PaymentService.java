package yoonleeverse.onlinecourseback.modules.payment.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentInput;

public interface PaymentService {

    ResultType payment(PaymentInput input);
}
