package spring.work.user.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.work.global.constant.ExceptionCode;
import spring.work.global.exception.BusinessException;
import spring.work.global.externalApi.workPoint.PointRequester;
import spring.work.global.rabbitmq.utils.ProducerHelperService;
import spring.work.global.security.utils.AuthenticationHelperService;
import spring.work.global.utils.UtilService;
import spring.work.user.dto.request.Signup;
import spring.work.user.mapper.UserMapper;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationHelperService authenticationHelperService;
    @Mock private ProducerHelperService producerHelperService;
    @Mock private UtilService utilService;
    @Mock private PointRequester pointRequester;

    @InjectMocks
    private UserServiceImpl userService;

    private Signup signup;

    @BeforeEach
    void setUp() {
        signup = new Signup();
        signup.setUserId("testId");
        signup.setEmail("testEmail");
        signup.setPassword("testPassword");
    }

    @Test
    @DisplayName("이미 존재하는 userID로 회원가입시 에러발생")
    void throw_exception_already_exist_userID() {
        // Given
        given(userMapper.existsByUserId("testId")).willReturn(1);

        // When
        assertThatThrownBy(() -> userService.signup(signup))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ExceptionCode.USER_EXIST.getMessage());

        // Then
        then(userMapper).should(times(1)).existsByUserId("testId");
    }

    @Test
    @DisplayName("비밀번호와 개인정보가 암호화 된다.")
    void password_personal_data_encrypted() {
        // Given
        given(userMapper.existsByUserId(anyString())).willReturn(0);

        // When
        userService.signup(signup);

        // Then
        then(passwordEncoder).should(times(1)).encode(anyString());
        then(utilService).should(atLeastOnce()).encrypt(anyString());
    }
}