package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import spring.work.global.kafka.dto.MailDto;
import spring.work.global.security.auth.AuthUser;
import spring.work.user.dto.request.Signup;

import java.util.Optional;

@Mapper
public interface UserAuthMapper {

    int existsByUserId(String userId);

    void sendMailFailHistory(MailDto mailDto);
}
