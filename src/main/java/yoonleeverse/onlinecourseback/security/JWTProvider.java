package yoonleeverse.onlinecourseback.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTProvider {

    private final Algorithm algorithm;
    private final UserDetailsService userDetailsService;

    public String createAuthToken(UserEntity user) {
        return createToken(user, 1800000L); // 5시간
    }

    public String createRefreshToken(UserEntity user) {
        return createToken(user, 259200000L); // 3일
    }

    private String createToken(UserEntity user, Long expireTime) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + expireTime);

        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiredAt)
                .withSubject(user.getEmail())
                .sign(algorithm);
    }

    public String verifyToken(String token) {
        return "test@test.com";
//        try {
//            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);
//            return decodedJWT.getSubject();
//        } catch (Exception ex) {
//            return null;
//        }
    }

    public Authentication getAuthentication(String email) {
        UserEntity userDetails = (UserEntity) userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
