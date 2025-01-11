package spring.work.global.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomFilterExceptionHandler extends OncePerRequestFilter {

    private final DefaultFilterResponseWriter defaultFilterResponseWriter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (BusinessException e) {
            defaultFilterResponseWriter.writeAuthErrorResponse(response, e.getExceptionCode());
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | IllegalArgumentException e) {
            defaultFilterResponseWriter.writeAuthErrorResponse(response, ExceptionCode.TOKEN_ERROR);
        } catch (ExpiredJwtException e) {
            defaultFilterResponseWriter.writeAuthErrorResponse(response, ExceptionCode.TOKEN_EXPIRE);
        } catch (UnsupportedJwtException e) {
            defaultFilterResponseWriter.writeAuthErrorResponse(response, ExceptionCode.TOKEN_UNSUPPORTED);
        } catch (Exception e) {
            defaultFilterResponseWriter.writeAuthErrorResponse(response, ExceptionCode.FAIL);
        }
    }
}
