package yoonleeverse.onlinecourseback.modules.payment.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import yoonleeverse.onlinecourseback.modules.payment.types.AccessTokenDTO;

import java.time.Instant;

@Repository
@RequiredArgsConstructor
public class AccessTokenRedisRepositoryImpl implements AccessTokenRedisRepository {

    private final RedisTemplate<String, AccessTokenDTO> redisTemplate;
    private final static String KEY = "accessToken";

    @Override
    public String getToken() {
        ValueOperations<String, AccessTokenDTO> valueOperations = redisTemplate.opsForValue();
        AccessTokenDTO accessToken = valueOperations.get(KEY);
        if (accessToken == null || accessToken.getExpiredAt() >= System.currentTimeMillis()) {
            return null;
        }

        return accessToken.getAccessToken();
    }

    @Override
    public void setToken(AccessTokenDTO accessToken) {
        Assert.notNull(accessToken, "accessTokenDTO is null");

        ValueOperations<String, AccessTokenDTO> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(KEY, accessToken);
        redisTemplate.expireAt(KEY, Instant.ofEpochSecond(accessToken.getExpiredAt()));
    }

}
