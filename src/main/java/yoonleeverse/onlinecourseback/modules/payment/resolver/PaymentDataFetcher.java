package yoonleeverse.onlinecourseback.modules.payment.resolver;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.payment.service.PaymentService;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentInput;
import yoonleeverse.onlinecourseback.modules.payment.types.PaymentType;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class PaymentDataFetcher {

    private final PaymentService paymentService;

    @DgsMutation
    @PreAuthorize("isAuthenticated()")
    public ResultType payment(@InputArgument PaymentInput input) {
        return paymentService.payment(input);
    }

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    public List<PaymentType> getPayments() {
        return paymentService.getPayments();
    }
}
