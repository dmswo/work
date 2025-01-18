package spring.work.global.security.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        setAuthenticationToRedis(authentication, login.getUserId(), tokenInfo.getRefreshToken());

        return tokenInfo;
    }

    @Override
    public void saveAuthentication(Authentication authentication, String token) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        String redisKey = authUser.getUserId();

        // 토큰 검증
        checkTokenStatus(redisKey, token);
        // 인증정보 SecurityContext 저장
        setAuthentication(authentication);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        // 블랙리스트(로그아웃 된 토큰) 저장 및 레디스 토큰 삭제
        setBlackListTokenAndDeleteToken(token, authUser.getUserId());
    }

    @Override
    public TokenInfo reissue(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);

        // RefreshToken 검증
        jwtTokenProvider.validateToken(token);

        // 신규 Refresh 토큰 및 Access 토큰 발급
        Authentication authentication = jwtTokenProvider.getAuthentication(token);
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        authUser.setDefaultInfo(authUser);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        // 인증정보 redis 및 securityContext에 저장
        setAuthenticationToRedis(authentication, authentication.getName(), tokenInfo.getRefreshToken());
        return tokenInfo;
    }

    private void checkTokenStatus(String redisKey, String token) {
        checkEmpty(token);
        checkLoggedOut(token);
        checkExpired(redisKey);
    }

    private void checkEmpty(String token) {
        if (token == null) {
            throw new BusinessException(ExceptionCode.TOKEN_EXPIRE);
        }
    }

    private void checkLoggedOut(String token) {
        String values = redisUtil.getValues(token);
        if ("logout".equals(values))
            throw new BusinessException(ExceptionCode.TOKEN_LOGOUT);
    }

    private void checkExpired(String redisKey) {
        String token = getToken(redisKey);
        checkEmpty(token);
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void setAuthenticationToRedis(Authentication authentication, String redisKey, String token) {
        // 토큰 redis 저장
        setToken(redisKey, token);

        // 인증정보 SecurityContext 저장
        setAuthentication(authentication);
    }

    private void setBlackListTokenAndDeleteToken(String token, String userId) {
        setToken(token, "logout");
        redisUtil.deleteValues(userId);
    }
}
