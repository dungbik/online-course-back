package yoonleeverse.onlinecourseback.modules.payment.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenDTO implements Serializable {

    @JsonProperty("access_token")
    private String accessToken;
    private Long now;
    @JsonProperty("expired_at")
    private Long expiredAt;
}