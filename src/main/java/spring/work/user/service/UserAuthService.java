package spring.work.user.service;

import jakarta.servlet.http.HttpServletRequest;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.TokenInfo;
import spring.work.global.kafka.dto.MailDto;
import spring.work.user.dto.request.Login;
import spring.work.user.dto.request.Signup;

public interface UserAuthService {

    ResultCode signup(Signup dto);

    TokenInfo login(Login login, String ip);

    ResultCode logout(HttpServletRequest request);

    TokenInfo reissue(HttpServletRequest request);

    void sendMailFailHistory(MailDto mailDto);
}
