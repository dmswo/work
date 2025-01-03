package spring.work.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class Login {
    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String loginId;
    @NotBlank(message = "로그인 비밀번호가 비어있습니다.")
    private String password;
}
