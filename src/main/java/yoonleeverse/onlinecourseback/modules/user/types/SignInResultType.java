package yoonleeverse.onlinecourseback.modules.user.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInResultType {
    private Boolean success;
    private String error;

    private String accessToken;

    private UserType user;

    public static SignInResultType success(String accessToken, UserEntity user) {
        return new SignInResultType(
                true, null,
                accessToken,
                new UserType(user.getName(), user.getEmail(), user.getEmailAgreed(), user.getAvatar().getFileUrl()));
    }

    public static SignInResultType fail(String error) {
        return SignInResultType.builder()
                .success(false)
                .error(error)
                .build();
    }
}