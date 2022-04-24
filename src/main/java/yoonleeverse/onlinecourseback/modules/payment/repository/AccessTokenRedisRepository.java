package yoonleeverse.onlinecourseback.modules.payment.repository;

import yoonleeverse.onlinecourseback.modules.payment.types.AccessTokenDTO;

public interface AccessTokenRedisRepository {

    String getToken();
    void setToken(AccessTokenDTO accessToken);
}
