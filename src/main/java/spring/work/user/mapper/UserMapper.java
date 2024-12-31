package spring.work.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import spring.work.user.request.SignupReqDto;

@Mapper
public interface UserMapper {

    void signup(SignupReqDto dto);
}
