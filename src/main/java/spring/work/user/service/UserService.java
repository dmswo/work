package spring.work.user.service;

import spring.work.global.constant.ResultCode;
import spring.work.user.request.SignupReqDto;

public interface UserService {

    ResultCode signup(SignupReqDto dto);
}
