package spring.work.global.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.user.mapper.UserMapper;

@Slf4j
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userMapper.selectAuthUserByUserId(username).orElseThrow(() -> new BusinessException(ExceptionCode.USERNAME_NOT_FOUND));
        authUser.setDefaultInfo(authUser);

        return authUser;
    }
}
