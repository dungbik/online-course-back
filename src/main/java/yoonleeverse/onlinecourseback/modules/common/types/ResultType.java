package yoonleeverse.onlinecourseback.modules.common.types;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultType {

    private Boolean success;
    private String error;

    public static ResultType success() {
        return new ResultType(true, null);
    }

    public static ResultType fail(String error) {
        return new ResultType(false, error);
    }
}
