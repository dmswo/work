package spring.work.user.service;

import jakarta.servlet.http.HttpServletRequest;
import spring.work.global.constant.ResultCode;
import spring.work.global.dto.TokenInfo;
import spring.work.global.externalApi.workPoint.dto.UserPointInfoApiResponse;
import spring.work.global.rabbitmq.dto.MailDto;
import spring.work.user.dto.request.Login;
import spring.work.user.dto.request.Signup;

public interface UserService {

    ResultCode signup(Signup dto);

    TokenInfo login(Login login, String ip);

    ResultCode logout(HttpServletRequest request);

    TokenInfo reissue(HttpServletRequest request);

    void sendMailFailHistory(MailDto mailDto);
}
