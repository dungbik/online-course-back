package yoonleeverse.onlinecourseback.modules.user.types;

import lombok.Data;

@Data
public class UpdateUserInput {

    private String name;

    private Boolean emailAgreed;
}
