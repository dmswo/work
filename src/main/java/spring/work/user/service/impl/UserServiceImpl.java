package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.work.global.exception.BusinessException;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.constant.ResultCode;
import spring.work.user.mapper.UserMapper;
import spring.work.user.request.SignupReqDto;
import spring.work.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResultCode signup(SignupReqDto dto) {
        try {
            dto.encodedPassword(passwordEncoder.encode(dto.getPassword()));
            userMapper.signup(dto);
            return ResultCode.OK;
        } catch (Exception e) {
            log.error("error signup : {}", e);
            throw new BusinessException(ExceptionCode.FAIL);
        }
    }
}
