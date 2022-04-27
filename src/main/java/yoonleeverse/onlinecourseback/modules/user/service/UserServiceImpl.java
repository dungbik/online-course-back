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
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinecourseback.config.AWSConfig;
import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.file.entity.FileEntity;
import yoonleeverse.onlinecourseback.modules.file.repository.FileRepository;
import yoonleeverse.onlinecourseback.modules.file.service.StorageService;
import yoonleeverse.onlinecourseback.modules.mail.EmailMessage;
import yoonleeverse.onlinecourseback.modules.mail.EmailService;
import yoonleeverse.onlinecourseback.modules.user.types.*;
import yoonleeverse.onlinecourseback.security.JWTProvider;
import yoonleeverse.onlinecourseback.modules.user.entity.AuthorityEntity;
import yoonleeverse.onlinecourseback.modules.user.repository.UserRepository;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

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
    private final FileRepository fileRepository;
    private final StorageService storageService;
    private final AWSConfig awsConfig;

    public UserEntity currentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (UserEntity) context.getAuthentication().getPrincipal();
    }

    @Override
    @Transactional(readOnly = true)
    public UserType getUser() {
        // todo mapper 클래스 만들어서 관리하는게 좋을듯
        return new UserType(currentUser(), awsConfig.getFileCloudUrl());
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

    @Override
    public ResultType updateUser(UpdateUserInput input) {
        try {
            UserEntity exUser = userRepository.findByEmail(currentUser().getEmail())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

            exUser.updateUser(input);

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType updateEmail(String email) {
        try {
            UserEntity exUser = userRepository.findByEmail(currentUser().getEmail())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

            exUser.updateEmail(email);
            emailService.sendEmail(EmailMessage.builder()
                    .to(exUser.getEmail())
                    .subject("이메일 인증")
                    .message(exUser.getVerifyCode())
                    .build());

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType updateAvatar(MultipartFile file) {
        try {
            UserEntity exUser = userRepository.findByEmail(currentUser().getEmail())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

            FileEntity exAvatar = exUser.getAvatar();
            String fileUrl = storageService.put(file, exUser.getName(), "public/avatar");

            if (exAvatar != null) {
                storageService.delete(exUser.getAvatar().getFileUrl());
                exAvatar.updateFileUrl(fileUrl);
            } else {
                FileEntity avatar = FileEntity.builder()
                        .fileUrl(fileUrl)
                        .build();
                fileRepository.save(avatar);

                exUser.updateAvatar(avatar);
            }

            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }

    @Override
    public ResultType removeUser() {
        try {
            UserEntity exUser = userRepository.findByEmail(currentUser().getEmail())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

            if (exUser.getAuthorities().contains(AuthorityEntity.ROLE_ADMIN))
                throw new RuntimeException("관리자 계정은 삭제할 수 없습니다.");

            FileEntity exAvatar = exUser.getAvatar();
            if (exAvatar != null) {
                storageService.delete(exUser.getAvatar().getFileUrl());
            }

            userRepository.delete(exUser);
            return ResultType.success();
        } catch (Exception e) {
            return ResultType.fail(e.getMessage());
        }
    }
}
