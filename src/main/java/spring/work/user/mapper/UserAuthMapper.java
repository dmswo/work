package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.security.auth.AuthUser;
import spring.work.user.dto.request.Signup;

import java.util.Optional;

@Mapper
public interface UserAuthMapper {

    void signup(Signup dto);

    Optional<AuthUser> selectAuthUserByUserId(String userId);

    int existsByUserId(String userId);

    void saveLoginHistory(@Param("userId") String userId, @Param("ip") String ip);

    void sendMailFailHistory(MailDto mailDto);
}
