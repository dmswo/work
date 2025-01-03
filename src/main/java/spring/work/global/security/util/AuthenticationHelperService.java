package spring.work.global.security.util;

import spring.work.global.dto.TokenInfo;
import spring.work.user.dto.request.Login;

public interface AuthenticationHelperService {

    TokenInfo processLoginAndReturnToken(Login login);
}
