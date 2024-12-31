package spring.work.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.work.global.exception.BusinessException;
import spring.work.global.exception.ExceptionCode;
import spring.work.global.response.ResultCode;
import spring.work.user.mapper.UserMapper;
import spring.work.user.request.SignupReqDto;
import spring.work.user.service.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public ResultCode signup(SignupReqDto dto) {
        try {
            userMapper.signup(dto);
            return ResultCode.OK;
        } catch (Exception e) {
            log.error("error : {}", e);
            throw new BusinessException(ExceptionCode.FAIL);
        }
    }
}
