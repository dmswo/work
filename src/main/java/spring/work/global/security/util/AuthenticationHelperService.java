package spring.work.global.security.util;

import org.springframework.security.core.Authentication;
import spring.work.global.dto.TokenInfo;
import spring.work.user.dto.request.Login;

public interface AuthenticationHelperService {

    void setToken(String userId, TokenInfo tokenInfo);
    TokenInfo processLoginAndReturnToken(Login login);
    void saveAuthentication(Authentication authentication);
}
