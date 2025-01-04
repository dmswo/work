package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.work.global.dto.TokenInfo;
import spring.work.global.exception.BusinessException;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.constant.ResultCode;
import spring.work.global.security.util.AuthenticationHelperService;
import spring.work.user.dto.request.Login;
import spring.work.user.mapper.UserMapper;
import spring.work.user.dto.request.Signup;
import spring.work.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationHelperService authenticationHelperService;

    @Override
    public ResultCode signup(Signup dto) {
        if (userMapper.existsByUserId(dto.getUserId()) > 0) {
            throw new BusinessException(ExceptionCode.USER_EXIST);
        }
        dto.encodedPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.signup(dto);
        return ResultCode.OK;
    }

    @Override
    public TokenInfo login(Login login) {
        TokenInfo tokenInfo = authenticationHelperService.processLoginAndReturnToken(login);

        // 접속 기록 저장 로직 추가 예정

        return tokenInfo;
    }
}
