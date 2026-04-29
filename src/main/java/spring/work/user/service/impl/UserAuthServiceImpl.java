package spring.work.user.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.work.global.dto.TokenInfo;
import spring.work.global.exception.BusinessException;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.constant.ResultCode;
import spring.work.global.externalApi.workPoint.PointRequester;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.kafka.service.ProducerHelperService;
import spring.work.global.security.utils.AuthenticationHelperService;
import spring.work.global.utils.UtilService;
import spring.work.user.dto.request.CreatePoint;
import spring.work.user.dto.request.Login;
import spring.work.user.entity.Users;
import spring.work.user.mapper.UserAuthMapper;
import spring.work.user.dto.request.Signup;
import spring.work.user.repository.UserRepository;
import spring.work.user.service.UserAuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    // jpa 전화 repository
    private final UserRepository userRepository;

    // mybatis 전환 mapper
    private final UserAuthMapper userAuthMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationHelperService authenticationHelperService;
    private final UtilService utilService;
    private final PointRequester pointRequester;
    private final ProducerHelperService producerHelperService;

    @Override
    @Transactional
    public ResultCode signup(Signup dto) {
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw new BusinessException(ExceptionCode.USER_EXIST);
        }

        dto.encodedPassword(passwordEncoder.encode(dto.getPassword()));
        dto.encryptData(
                utilService.encrypt(dto.getEmail()),
                utilService.encrypt(dto.getPhone()),
                utilService.encrypt(dto.getAddress()));

        //
        Users users = Users.create(dto);
        users.setSignUser(dto.getUserId(), LocalDateTime.now(), dto.getUserId(), LocalDateTime.now());
        userRepository.save(users);

        // 포인트 데이터 생성
        //pointRequester.createUserPoint(CreatePoint.builder().userId(dto.getUserId()).build());

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
        userAuthMapper.saveLoginHistory(login.getUserId(), ip);
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
        userAuthMapper.sendMailFailHistory(mailDto);
    }
}
