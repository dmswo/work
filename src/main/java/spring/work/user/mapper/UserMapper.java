package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import spring.work.global.security.auth.AuthUser;
import spring.work.user.dto.request.Signup;

import java.util.Optional;

@Mapper
public interface UserMapper {

    void signup(Signup dto);

    Optional<AuthUser> selectAuthUserByUserId(String userId);

    int existsByUserId(String userId);
}
