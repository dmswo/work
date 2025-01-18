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
import spring.work.global.rabbitmq.dto.MessageDto;
import spring.work.global.rabbitmq.utils.ProducerHelperService;
import spring.work.global.security.utils.AuthenticationHelperService;
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

    @Override
    public ResultCode signup(Signup dto) {
        if (userMapper.existsByUserId(dto.getUserId()) > 0) {
            throw new BusinessException(ExceptionCode.USER_EXIST);
        }
        dto.encodedPassword(passwordEncoder.encode(dto.getPassword()));
        userMapper.signup(dto);

        // 회원가입 알림 메일 발송
        MessageDto messageDto = new MessageDto();
        messageDto.setSubject("제목");
        messageDto.setContent("내용");
        messageDto.setToEmail("dmswo106@naver.com");
        producerHelperService.sendMail(messageDto);

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
    public String eventMq(MessageDto messageDto) {
        producerHelperService.eventSendMessage(messageDto);
        return "OK";
    }

    @Override
    public String productMq(MessageDto messageDto) {
        producerHelperService.productSendMessage(messageDto);
        return "OK";
    }

    @Override
    public String ticketMq(MessageDto messageDto) {
        producerHelperService.ticketSendMessage(messageDto);
        return "OK";
    }
}
