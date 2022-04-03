package yoonleeverse.onlinecourseback.user.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.user.entity.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResultType {
    private Boolean success;
    private String error;

    private String accessToken;

    private String name;
    private String email;
    private Boolean emailAgreed;
    private String avatar;

    public static SignInResultType success(String accessToken, UserEntity user) {
        return new SignInResultType(
                true, null,
                accessToken,
                user.getName(), user.getEmail(), user.getEmailAgreed(), user.getAvatar());
    }

    public static SignInResultType fail(String error) {
        return SignInResultType.builder()
                .success(false)
                .error(error)
                .build();
    }
}