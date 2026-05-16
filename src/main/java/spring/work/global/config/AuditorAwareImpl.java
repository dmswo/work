package spring.work.global.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import spring.work.global.security.auth.AuthUser;

import java.util.Optional;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("anonymousUser");
        }

        Object principal = authentication.getPrincipal();

        // 일반 로그인 사용자
        if (principal instanceof AuthUser authUser) {
            return Optional.of(authUser.getUsername());
        }

        // 회원가입 시 임시 authentication
        if (principal instanceof String userId) {
            return Optional.of(userId);
        }

        return Optional.of("anonymousUser");
    }
}