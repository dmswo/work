package spring.work.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
public class Signup {

    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String userId;

    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    public void encodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }


}
