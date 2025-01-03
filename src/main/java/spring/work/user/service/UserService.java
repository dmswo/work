package spring.work.user.service;

import spring.work.global.constant.ResultCode;
import spring.work.global.dto.TokenInfo;
import spring.work.user.dto.request.Login;
import spring.work.user.dto.request.Signup;

public interface UserService {

    ResultCode signup(Signup dto);

    TokenInfo login(Login login);
}
