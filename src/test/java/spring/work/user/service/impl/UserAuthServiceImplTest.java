package spring.work.user.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.dto.TokenInfo;
import spring.work.global.exception.BusinessException;
import spring.work.global.externalApi.workPoint.PointRequester;
import spring.work.global.rabbitmq.dto.MailDto;
import spring.work.global.rabbitmq.utils.ProducerHelperService;
import spring.work.global.security.utils.AuthenticationHelperService;
import spring.work.global.utils.UtilService;
import spring.work.user.dto.request.CreatePoint;
import spring.work.user.dto.request.Login;
import spring.work.user.dto.request.Signup;
import spring.work.user.dto.response.CreatePointResponse;
import spring.work.user.mapper.UserAuthMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceImplTest {

    @Mock private UserAuthMapper userAuthMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationHelperService authenticationHelperService;
    @Mock private ProducerHelperService producerHelperService;
    @Mock private UtilService utilService;
    @Mock private PointRequester pointRequester;

    @InjectMocks
    private UserAuthServiceImpl userAuthService;

    private Signup signup;

    @BeforeEach
    void setUp() {
        signup = Signup.builder()
                .userId("testId")
                .email("testEmail")
                .password("testPassword")
                .phone("01012345678")
                .address("서울시")
                .build();
    }

    @Test
    @DisplayName("이미 존재하는 userID로 회원가입시 에러발생")
    void throw_exception_already_exist_userID() {
        // Given
        given(userAuthMapper.existsByUserId("testId")).willReturn(1);

        // When
        assertThatThrownBy(() -> userAuthService.signup(signup))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_EXIST.getMessage());

        // Then
        then(userAuthMapper).should(times(1)).existsByUserId("testId");
    }

    @Test
    @DisplayName("비밀번호와 개인정보가 암호화 된다.")
    void password_personal_data_encrypted() {
        // Given
        given(userAuthMapper.existsByUserId(anyString())).willReturn(0);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");
        given(utilService.encrypt(anyString())).willReturn("encryptedData");
        given(utilService.decrypt(anyString())).willReturn("encryptedData");
        stubExternalDependencies();

        // When
        userAuthService.signup(signup);

        // Then
        then(passwordEncoder).should(times(1)).encode("testPassword");
        then(utilService).should(times(3)).encrypt(anyString());
        then(userAuthMapper).should(times(1)).signup(argThat(dto ->
                dto.getEmail().equals("encryptedData") &&
                        dto.getPhone().equals("encryptedData") &&
                        dto.getAddress().equals("encryptedData")
        ));
    }

    @Test
    @DisplayName("포인트 데이터가 생성된다.")
    void make_point() {
        // Given
        given(userAuthMapper.existsByUserId(anyString())).willReturn(0);
        stubExternalDependencies();

        // When
        userAuthService.signup(signup);

        // Then
        then(pointRequester).should(times(1)).createUserPoint(any(CreatePoint.class));
    }

    @Test
    @DisplayName("메일이 정상적으로 발송된다.")
    void send_mail() {
        // Given
        given(userAuthMapper.existsByUserId(anyString())).willReturn(0);
        stubExternalDependencies();

        // When
        userAuthService.signup(signup);

        // Then
        then(producerHelperService).should(times(1)).sendMail(any(MailDto.class));
    }

    private void stubExternalDependencies() {
        // 외부 의존성 stub
        given(pointRequester.createUserPoint(any(CreatePoint.class)))
                .willReturn(new CreatePointResponse());
        willDoNothing().given(producerHelperService).sendMail(any(MailDto.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // Given
        Login login = Login.builder()
                .userId("dmswo")
                .password("1234")
                .build();
        String ip = "127.0.0.1";
        TokenInfo tokenInfo = new TokenInfo("Bearer", "token", "refresh");
        given(authenticationHelperService.processLoginAndReturnToken(any(Login.class))).willReturn(tokenInfo);

        // When
        TokenInfo result = userAuthService.login(login, ip);

        // Then
        then(authenticationHelperService).should(times(1)).processLoginAndReturnToken(any(Login.class));
        then(userAuthMapper).should(times(1)).saveLoginHistory(login.getUserId(), ip);
        assertThat(result).isEqualTo(tokenInfo);
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class); // Mock 객체 생성

        // When
        userAuthService.logout(request);

        // Then
        then(authenticationHelperService).should(times(1)).logout(any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("토큰재발행 성공")
    void reissue_success() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class); // Mock 객체 생성

        // When
        userAuthService.reissue(request);

        // Then
        then(authenticationHelperService).should(times(1)).reissue(any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("메일발송 실패 히스토리 저장성공")
    void send_mail_fail_history_success() {
        // Given
        MailDto mail = MailDto.signupMailOf(signup);

        // When
        userAuthService.sendMailFailHistory(mail);

        // Then
        then(userAuthMapper).should(times(1)).sendMailFailHistory(any(MailDto.class));
    }
}