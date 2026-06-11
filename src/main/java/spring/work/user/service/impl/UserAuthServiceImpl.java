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
import spring.work.global.kafka.dto.MailEvent;
import spring.work.global.kafka.producer.EventProducer;
import spring.work.global.security.utils.AuthenticationHelperService;
import spring.work.global.utils.UtilService;
import spring.work.user.dto.request.CreatePoint;
import spring.work.user.dto.request.Login;
import spring.work.user.entity.UserLoginHistory;
import spring.work.user.entity.Users;
import spring.work.user.dto.request.Signup;
import spring.work.user.repository.UserLoginHistoryRepository;
import spring.work.user.repository.UserRepository;
import spring.work.user.service.UserAuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthServiceImpl implements UserAuthService {

    private final UserRepository userRepository;
    private final UserLoginHistoryRepository userLoginHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationHelperService authenticationHelperService;
    private final UtilService utilService;
    private final PointRequester pointRequester;
    private final EventProducer eventProducer;

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

        try {
            // 회원가입 시에는 로그인 정보가 없으므로 auditing용 authentication 임시 세팅
            authenticationHelperService
                    .setTemporaryAuthentication(dto.getUserId());

            Users users = Users.create(dto);
            userRepository.save(users);
        } finally {
            authenticationHelperService.clearAuthentication();
        }

        // 포인트 데이터 생성
        pointRequester.createUserPoint(CreatePoint.builder().userId(dto.getUserId()).build());

        // 회원가입 알림 메일 발송
        dto.decryptEmail(utilService.decrypt(dto.getEmail()));
        dto.changeUserId(dto.getUserId());
        MailEvent event = MailEvent.from(dto);

        eventProducer.send(event);

        return ResultCode.OK;
    }

    @Override
    public TokenInfo login(Login login, String ip) {
        TokenInfo tokenInfo = authenticationHelperService.processLoginAndReturnToken(login);

        Users user = userRepository.findByUserId(login.getUserId())
                .orElseThrow(() -> new BusinessException(ExceptionCode.USER_NOT_FOUND));

        UserLoginHistory history = UserLoginHistory.create(user, ip);
        history.setBaseInfo(login.getUserId(), LocalDateTime.now(), login.getUserId(), LocalDateTime.now());
        userLoginHistoryRepository.save(history);

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
}
