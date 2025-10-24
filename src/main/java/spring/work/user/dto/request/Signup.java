package spring.work.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import spring.work.user.constant.UserRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Signup {
    @Schema(defaultValue = "dmswo106")
    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    private String userId;

    @Schema(defaultValue = "1234")
    @NotBlank(message = "비밀번호가 비어있습니다.")
    private String password;

    @Schema(defaultValue = "1234")
    @NotBlank(message = "비밀번호 확인란이 비어있습니다.")
    private String passwordCheck;

    @Schema(defaultValue = "도토리국수")
    @NotBlank(message = "닉네임이 비어있습니다.")
    private String nickname;

    @Schema(defaultValue = "dmswo106@naver.com")
    @NotBlank(message = "이메일이 비어있습니다.")
    private String email;

    @Schema(defaultValue = "01012345678")
    @NotBlank(message = "전화번호가 비어있습니다.")
    private String phone;

    @Schema(defaultValue = "서울시 성북구 종암로2837가길78 이리저리아파트 101동 832호")
    @NotBlank(message = "주소가 비어있습니다.")
    private String address;

    @NotNull(message = "권한이 비어있습니다.")
    private UserRole userRole;

    public void encodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void encryptData(String email, String phone, String address) {
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public void decryptEmail(String email) {
        this.email = email;
    }

    public void changeUserId(String userId) {
        this.userId = userId;
    }
}
