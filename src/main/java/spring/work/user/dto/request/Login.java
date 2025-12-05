package spring.work.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Login {
    @NotBlank(message = "로그인 아이디가 비어있습니다.")
    @Schema(defaultValue = "dmswo106")
    private String userId;
    @NotBlank(message = "로그인 비밀번호가 비어있습니다.")
    @Schema(defaultValue = "1234")
    private String password;
}
