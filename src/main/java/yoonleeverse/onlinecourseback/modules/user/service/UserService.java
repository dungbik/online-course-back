package yoonleeverse.onlinecourseback.modules.user.service;

import yoonleeverse.onlinecourseback.modules.common.types.ResultType;
import yoonleeverse.onlinecourseback.modules.user.types.*;
import yoonleeverse.onlinecourseback.user.types.*;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    UserType getUser();

    ResultType signUp(SignUpInput input);

    SignInResultType signIn(SignInInput input, HttpServletResponse response);

    ResultType verify(String code);

    ReIssueResultType reIssue(String refreshToken, HttpServletResponse response);
}
