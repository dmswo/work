package spring.work.global.security.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.user.entity.Users;
import spring.work.user.mapper.UserAuthMapper;
import spring.work.user.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUserId(username).orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));
        AuthUser authUser = AuthUser.builder()
                .userId(users.getUserId())
                .password(users.getPassword())
                .nickName(users.getNickname())
                .userRole(users.getUserRole())
                .build();
        authUser.setDefaultInfo(authUser);
        return authUser;
    }
}
