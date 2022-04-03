package yoonleeverse.onlinecourseback.modules.user.types;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReIssueResultType {
    private Boolean success;
    private String error;

    private String accessToken;

    public static ReIssueResultType success(String accessToken) {
        return new ReIssueResultType(
                true, null,
                accessToken);
    }

    public static ReIssueResultType fail(String error) {
        return ReIssueResultType.builder()
                .success(false)
                .error(error)
                .build();
    }
}