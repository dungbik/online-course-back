package yoonleeverse.onlinecourseback.modules.user.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.modules.user.entity.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserType {
    private String name;
    private String email;
    private Boolean emailAgreed;
    private String avatar;

    public UserType(UserEntity user, String cloudUrl) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.emailAgreed = user.getEmailAgreed();
        if (user.getAvatar() != null) {
            this.avatar = String.format("%s/%s", cloudUrl, user.getAvatar().getFileUrl());
        }
    }
}
