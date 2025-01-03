package spring.work.global.security.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import spring.work.global.dto.TokenInfo;
import spring.work.global.security.jwt.JwtTokenProvider;
import spring.work.user.dto.request.Login;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationHelperServiceImpl implements AuthenticationHelperService{

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenInfo processLoginAndReturnToken(Login login) {
        // 인증 시도
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login.getLoginId(), login.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 토큰 생성


        return null;
    }
}
