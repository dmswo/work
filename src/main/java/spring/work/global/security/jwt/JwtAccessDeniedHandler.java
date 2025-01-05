package spring.work.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import spring.work.global.constant.ExceptionCode;

@Slf4j
@Component
public class JwtAccessDeniedHandler extends AbstractAuthResponseWriter implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        writeAuthErrorResponse(response, ExceptionCode.FORBIDDEN);
    }
}
