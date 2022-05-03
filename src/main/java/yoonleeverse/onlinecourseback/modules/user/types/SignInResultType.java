package yoonleeverse.onlinecourseback.modules.user.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResultType {
    private Boolean success;
    private String error;

    private String accessToken;

    private UserType user;

    public static SignInResultType success(String accessToken, UserType user) {

        return new SignInResultType(true, null, accessToken, user);
    }

    public static SignInResultType fail(String error) {
        return SignInResultType.builder()
                .success(false)
                .error(error)
                .build();
    }
}