package spring.work.global.security.jwt;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.MediaType;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.response.ApiResponse;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public abstract class AbstractAuthResponseWriter {
    protected static final Gson GSON = new Gson();

    protected void writeAuthErrorResponse(HttpServletResponse response, ExceptionCode exceptionCode) {
        String content = GSON.toJson(ApiResponse.errorResponse(exceptionCode));
        writeResponse(response, content);
    }

    private void writeResponse(HttpServletResponse response, String content) {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (PrintWriter writer = response.getWriter()) {
            writer.write(content);
        } catch (Exception e) {
            throw new BusinessException(ExceptionCode.FAIL);
        }
    }
}
