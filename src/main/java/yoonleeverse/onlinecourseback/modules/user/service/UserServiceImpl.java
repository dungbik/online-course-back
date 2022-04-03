package yoonleeverse.onlinecourseback.modules.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.mail.EmailMessage;
import yoonleeverse.onlinecourseback.modules.mail.EmailService;
import yoonleeverse.onlinecourseback.modules.user.types.*;
import yoonleeverse.onlinecourseback.security.JWTProvider;
import yoonleeverse.onlinecourseback.modules.user.entity.AuthorityEntity;
import yoonleeverse.onlinecourseback.modules.user.repository.UserRepository;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;
import yoonleeverse.onlinecourseback.user.types.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationProvider;
    private final JWTProvider jwtProvider;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public UserType getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserEntity user = (UserEntity) context.getAuthentication().getPrincipal();
        return new UserType(user);
    }

    @Override
    public ResultType signUp(SignUpInput input) {
        if (userRepository.existsByEmail(input.getEmail()))
            return ResultType.fail("사용중인 이메일입니다.");

        UserEntity user = UserEntity.builder()
                .name(input.getName())
                .email(input.getEmail())
                .emailAgreed(input.getEmailAgreed())
                .password(passwordEncoder.encode(input.getPassword()))
                .authorities(Collections.singleton(AuthorityEntity.ROLE_USER))
                .build();

        userRepository.save(user);
        emailService.sendEmail(EmailMessage.builder()
                .to(user.getEmail())
                .subject("이메일 인증")
                .message(user.getVerifyCode())
                .build());

        return ResultType.success();
    }

    @Override
    @Transactional(readOnly = true)
    public SignInResultType signIn(SignInInput input, HttpServletResponse response) {
        try {
            UsernamePasswordAuthenticationToken credentials =
                    new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword());

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(authenticationProvider.authenticate(credentials));

            UserEntity user = (UserEntity) context.getAuthentication().getPrincipal();

            Cookie cookie = new Cookie("x-refresh", jwtProvider.createRefreshToken(user));
            cookie.setHttpOnly(true);

            response.addCookie(cookie);

            return SignInResultType.success(jwtProvider.createAuthToken(user), user);
        } catch (LockedException e) {
            return SignInResultType.fail("이메일 인증이 완료되지 않았습니다.");
        } catch (Exception e) {
            return SignInResultType.fail("이메일 혹은 비밀번호가 틀렸습니다.");
        }
    }

    @Override
    public ResultType verify(String code) {
        try {
            UserEntity user = userRepository.findByVerifyCode(code)
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 코드입니다."));

            user.verifyEmail();
            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReIssueResultType reIssue(String refreshToken, HttpServletResponse response) {
        try {
            String email = jwtProvider.verifyToken(refreshToken);
            if (email == null)
                throw new RuntimeException("유효하지 않은 토큰입니다.");

            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

            Cookie cookie = new Cookie("x-refresh", jwtProvider.createRefreshToken(user));
            cookie.setHttpOnly(true);

            response.addCookie(cookie);

            return ReIssueResultType.success(jwtProvider.createAuthToken(user));
        } catch (Exception e) {
            return ReIssueResultType.fail(e.getMessage());
        }
    }
}
