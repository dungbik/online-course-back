package yoonleeverse.onlinecourseback.modules.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEnrollmentEntity;
import yoonleeverse.onlinecourseback.modules.course.entity.CourseEntity;
import yoonleeverse.onlinecourseback.modules.course.mapper.CourseMapper;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseEnrollmentRepository;
import yoonleeverse.onlinecourseback.modules.course.repository.CourseRepository;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentEntity;
import yoonleeverse.onlinecourseback.modules.payment.entity.PaymentStatus;
import yoonleeverse.onlinecourseback.modules.payment.repository.AccessTokenRedisRepository;
import yoonleeverse.onlinecourseback.modules.payment.repository.PaymentRepository;
import yoonleeverse.onlinecourseback.modules.payment.types.*;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Value("${config.imp.key}")
    private String impKey;

    @Value("${config.imp.secret}")
    private String impSecret;

    private final RestTemplate restTemplate;
    private final AccessTokenRedisRepository accessTokenRepository;
    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final CourseMapper courseMapper;
    private final CourseEnrollmentRepository enrollmentRepository;

    private UserEntity currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserEntity) context.getAuthentication().getPrincipal();
    }

    private AccessTokenDTO callToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject body = new JSONObject();
        body.put("imp_key", impKey);
        body.put("imp_secret", impSecret);
        HttpEntity<JSONObject> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<ApiResponseDTO<AccessTokenDTO>> tokenInfo = restTemplate.exchange(
                    "https://api.iamport.kr/users/getToken", HttpMethod.POST, request,
                    new ParameterizedTypeReference<ApiResponseDTO<AccessTokenDTO>>() { }
            );

            return tokenInfo.getBody().getResponse();
        } catch (Exception e) {
            throw new RuntimeException("accessToken 가져오기 실패");
        }
    }

    private String getToken() {
        String accessToken = accessTokenRepository.getToken();

        if (accessToken == null) {
            AccessTokenDTO accessTokenDTO = callToken();
            accessToken = accessTokenDTO.getAccessToken();
            if (accessToken == null)
                throw new RuntimeException("accessToken is null");

            accessTokenRepository.setToken(accessTokenDTO);
        }

        return accessToken;
    }

    public PaymentsDTO getPayments(String impUid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", String.format("Bearer %s", getToken()));
        HttpEntity<JSONObject> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<ApiResponseDTO<PaymentsDTO>> paymentInfo = restTemplate.exchange(
                    String.format("https://api.iamport.kr/payments/%s", impUid), HttpMethod.POST, request,
                    new ParameterizedTypeReference<ApiResponseDTO<PaymentsDTO>>() { }
            );

            return paymentInfo.getBody().getResponse();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(String.format("userId: %s, impUid: %s의 결제 내역 가져오기 실패", currentUser().getUserId(), impUid));
        }
    }

    @Async
    public void failPayment(Long merchantUid) {
        try {
            PaymentEntity payment = paymentRepository.findByMerchantUid(merchantUid)
                    .orElseThrow(() -> new RuntimeException(
                            String.format("merchantUid: %ld는 존재하지 않는 거래내역입니다.", merchantUid)));
            payment.fail();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void successPayment(Long merchantUid) {
        try {
            PaymentEntity payment = paymentRepository.findByMerchantUid(merchantUid)
                    .orElseThrow(() -> new RuntimeException(
                            String.format("merchantUid: %ld는 존재하지 않는 거래내역입니다.", merchantUid)));
            payment.success();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public ResultType payment(PaymentInput input) {
        try {
            UserEntity user = currentUser();

            CourseEntity course = courseRepository.findBySlug(input.getSlug())
                    .orElseThrow(() -> new RuntimeException(String.format(
                            "userId: %s, slug: %s는 존재하지 않는 강의입니다.",
                            user.getUserId(), input.getSlug()))
                    );

            List<PaymentEntity> paymentHistories = paymentRepository.findByUserAndCourse(user, course);

            for (PaymentEntity paymentHistory : paymentHistories) {
                if (paymentHistory.getStatus().equals(PaymentStatus.SUCCESS))
                    throw new RuntimeException(String.format(
                            "userId: %s, slug: %s는 결제된 강의입니다.",
                            user.getUserId(), input.getSlug())
                    );
            }

            PaymentsDTO payments = getPayments(input.getImpUid());
            if (input.getAmount() != payments.getAmount())
                throw new RuntimeException(String.format(
                        "userId: %s, slug: %s는 가격이 변조되었습니다.",
                        user.getUserId(), input.getSlug())
                );

            CourseEnrollmentEntity enrollment = CourseEnrollmentEntity.builder()
                    .user(user).course(course).build();

            PaymentEntity payment = PaymentEntity.makePayment(user, course, enrollment, payments);
            paymentRepository.save(payment);

            successPayment(input.getMerchantUid());
            return ResultType.success();
        } catch (Exception e) {
            failPayment(input.getMerchantUid());
            log.error(e.getMessage());
            return ResultType.fail(e.getMessage());
        }
    }

    /**
     * todo CourseType은 DataLoader로 처리되게
     */
    @Override
    public List<PaymentType> getPayments() {
        return paymentRepository.findAllByUser(currentUser()).stream()
                .map((payment) -> new PaymentType(payment, courseMapper.toDTO(payment.getCourse())))
                .collect(Collectors.toList());
    }
}
