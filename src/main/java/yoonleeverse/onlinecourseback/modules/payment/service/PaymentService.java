package yoonleeverse.onlinecourseback.modules.payment.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentInput;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentType;

import java.util.List;

public interface PaymentService {

    ResultType payment(PaymentInput input);

    List<PaymentType> getPayments();
}
