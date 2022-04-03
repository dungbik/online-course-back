package yoonleeverse.onlinecourseback.user.resolver;

import com.netflix.graphql.dgs.*;
import com.netflix.graphql.dgs.internal.DgsWebMvcRequestData;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.context.request.ServletWebRequest;
import yoonleeverse.onlinecourseback.common.types.ResultType;
import yoonleeverse.onlinecourseback.user.service.UserService;
import yoonleeverse.onlinecourseback.user.types.*;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class UserResolver {

    private final UserService userService;

    @DgsQuery
    @PreAuthorize("isAuthenticated()")
    public UserType getUser() {
        return userService.getUser();
    }

    @DgsMutation
    public ResultType signUp(@InputArgument SignUpInput input) {
        return userService.signUp(input);
    }

    @DgsMutation
    public SignInResultType signIn(@InputArgument SignInInput input, DgsDataFetchingEnvironment dfe) {
        DgsWebMvcRequestData requestData = (DgsWebMvcRequestData) dfe.getDgsContext().getRequestData();
        ServletWebRequest webRequest = (ServletWebRequest) requestData.getWebRequest();
        return userService.signIn(input, webRequest.getResponse());
    }

    @DgsMutation
    public ResultType verifyEmail(@InputArgument String code) {
        return userService.verify(code);
    }

    @DgsMutation
    public ReIssueResultType reIssue(@CookieValue(name = "x-refresh") String refreshToken,
                                     DgsDataFetchingEnvironment dfe) {
        DgsWebMvcRequestData requestData = (DgsWebMvcRequestData) dfe.getDgsContext().getRequestData();
        ServletWebRequest webRequest = (ServletWebRequest) requestData.getWebRequest();
        return userService.reIssue(refreshToken, webRequest.getResponse());
    }
}
