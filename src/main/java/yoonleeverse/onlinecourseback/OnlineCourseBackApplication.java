package yoonleeverse.onlinecourseback;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinecourseback.modules.user.entity.AuthorityEntity;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;
import yoonleeverse.onlinecourseback.modules.user.repository.UserRepository;

import java.util.Collections;

@SpringBootApplication(exclude = { RedisRepositoriesAutoConfiguration.class })
@EnableJpaAuditing
public class OnlineCourseBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCourseBackApplication.class, args);
    }

}

@RequiredArgsConstructor
@Component
class DBInit implements InitializingBean {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!userRepository.findByEmail("test@test.com").isPresent()) {
            UserEntity user = UserEntity.builder()
                    .email("test@test.com")
                    .name("관리자")
                    .emailAgreed(false)
                    .password(passwordEncoder.encode("test"))
                    .authorities(Collections.singleton(AuthorityEntity.ROLE_ADMIN))
                    .build();

            userRepository.save(user);
        }
    }
}
