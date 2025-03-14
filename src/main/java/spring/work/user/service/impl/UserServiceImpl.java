package spring.work.user.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.work.global.dto.TokenInfo;
import spring.work.global.exception.BusinessException;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.constant.ResultCode;
import spring.work.global.externalApi.workPoint.PointRequester;
import spring.work.global.rabbitmq.dto.MailDto;
import spring.work.global.rabbitmq.utils.ProducerHelperService;
import spring.work.global.security.utils.AuthenticationHelperService;
import spring.work.global.utils.UtilService;
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
    private final ProducerHelperService producerHelperService;
    private final UtilService utilService;
    private final PointRequester pointRequester;

    @Override
    public ResultCode signup(Signup dto) {
        if (userMapper.existsByUserId(dto.getUserId()) > 0) {
            throw new BusinessException(ExceptionCode.USER_EXIST);
        }

        dto.encodedPassword(passwordEncoder.encode(dto.getPassword()));
        dto.encryptData(
                utilService.encrypt(dto.getEmail()),
                utilService.encrypt(dto.getPhone()),
                utilService.encrypt(dto.getAddress()));

        userMapper.signup(dto);

        // 회원가입 알림 메일 발송
        dto.decryptEmail(utilService.decrypt(dto.getEmail()));
        dto.changeUserId(dto.getUserId());
        MailDto signupMail = MailDto.signupMailOf(dto);

        producerHelperService.sendMail(signupMail);

        return ResultCode.OK;
    }

    @Override
    public TokenInfo login(Login login, String ip) {
        TokenInfo tokenInfo = authenticationHelperService.processLoginAndReturnToken(login);
        userMapper.saveLoginHistory(login.getUserId(), ip);
        return tokenInfo;
    }

    @Override
    public ResultCode logout(HttpServletRequest request) {
        authenticationHelperService.logout(request);
        return ResultCode.OK;
    }

    @Override
    public TokenInfo reissue(HttpServletRequest request) {
        return authenticationHelperService.reissue(request);
    }

    @Override
    public void sendMailFailHistory(MailDto mailDto) {
        userMapper.sendMailFailHistory(mailDto);
    }
}
