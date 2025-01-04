package spring.work.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import spring.work.user.constant.UserRole;

@Getter
public class Signup {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    @NotNull(message = "권한이 비어있습니다.")
    private UserRole userRole;

    public void encodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }


}
