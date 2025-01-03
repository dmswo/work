package spring.work.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.TokenInfo;
import spring.work.global.exception.BusinessException;
import spring.work.global.security.auth.AuthUser;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String BEARER_TYPE = "Bearer";

    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Authentication authentication) {
        AuthUser authUser = (AuthUser) authentication.getPrincipal();
        Map<String, Object> properties = authUser.getProperties();

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .addClaims(properties)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenInfo.builder().grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        String loginId = (String) claims.get("loginId");
        String nickName = (String) claims.get("nickName");

        if (claims.get("11") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("11").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails를 상속받은 AuthUser 객체를 만들어서 Authentication 리턴
        AuthUser authUser = AuthUser.builder()
                .loginId(loginId)
                .nickName(nickName)
                .build();

        return new UsernamePasswordAuthenticationToken(authUser, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
            throw e;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
            throw e;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            log.error("parseClaims error :: {}", e);
            throw new BusinessException(ExceptionCode.TOKEN_ERROR);
        }
    }
}
