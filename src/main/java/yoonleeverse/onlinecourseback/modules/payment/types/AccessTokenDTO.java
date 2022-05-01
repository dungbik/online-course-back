package yoonleeverse.onlinecourseback.modules.payment.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenDTO implements Serializable {

    private String accessToken;
    private Long now;
    private Long expiredAt;
}