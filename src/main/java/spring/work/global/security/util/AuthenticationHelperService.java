package spring.work.global.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import spring.work.global.dto.TokenInfo;
import spring.work.user.dto.request.Login;

public interface AuthenticationHelperService {
    String getToken(String userId);
    void setToken(String userId, String token);

    TokenInfo processLoginAndReturnToken(Login login);
    void saveAuthentication(Authentication authentication, String token);

    void logout(HttpServletRequest request);

    TokenInfo reissue(HttpServletRequest request);
}
