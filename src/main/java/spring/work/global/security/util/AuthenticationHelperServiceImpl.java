package spring.work.global.security.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.TokenInfo;
import spring.work.global.exception.BusinessException;
import spring.work.global.redis.RedisUtil;
import spring.work.global.security.auth.AuthUser;
import spring.work.global.security.jwt.JwtTokenProvider;
import spring.work.user.dto.request.Login;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationHelperServiceImpl implements AuthenticationHelperService {

    private final RedisUtil redisUtil;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String getToken(String redisKey) {
        return redisUtil.getValues(redisKey);
    }

    @Override
    public void setToken(String redisKey, String token) {
        redisUtil.setValues(redisKey, token);
    }

    @Override
    public TokenInfo processLoginAndReturnToken(Login login) {
        // 인증 시도
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getUserId(), login.getPassword());

        // authenticate 메소드 실행 시 AuthUserDetailsService의 loadUserByUsername 메서드가 실행됨.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 인증 정보 저장(redis 및 SecurityContext)
        setAuthenticationToRedis(authentication, login.getUserId(), tokenInfo.getAccessToken());

        return tokenInfo;
    }

    @Override
    public void saveAuthentication(Authentication authentication, String token) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String redisKey = authUser.getUserId();

        checkTokenStatus(redisKey, token);
        setAuthenticationToRedis(authentication, redisKey, token);
    }

    private void checkTokenStatus(String redisKey, String token) {
        checkEmpty(token);
        checkExpired(redisKey);
    }

    private void checkEmpty(String token) {
        if (token == null) {
            throw new BusinessException(ExceptionCode.TOKEN_EXPIRE);
        }
    }

    private void checkExpired(String redisKey) {
        String token = getToken(redisKey);
    }

    /**
     * 인증정보 redis 및 securityContext에 저장
     */
    private void setAuthenticationToRedis(Authentication authentication, String redisKey, String token) {
        // 토큰 redis 저장
        setToken(redisKey, token);

        // 인증정보 SecurityContext 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
