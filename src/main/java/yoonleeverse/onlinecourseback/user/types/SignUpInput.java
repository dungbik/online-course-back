package yoonleeverse.onlinecourseback.user.types;

import lombok.Data;

@Data
public class SignUpInput {

    private String name;
    private String email;
    private Boolean emailAgreed;
    private String password;
}
