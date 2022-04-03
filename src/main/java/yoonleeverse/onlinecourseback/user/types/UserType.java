package yoonleeverse.onlinecourseback.user.types;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import yoonleeverse.onlinecourseback.user.entity.UserEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserType {
    private String name;
    private String email;
    private Boolean emailAgreed;
    private String avatar;

    public UserType(UserEntity user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.emailAgreed = user.getEmailAgreed();
        this.avatar = user.getAvatar();
    }
}
