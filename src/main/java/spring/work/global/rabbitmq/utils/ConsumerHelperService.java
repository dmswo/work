package spring.work.global.rabbitmq.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import spring.work.global.rabbitmq.dto.MailDto;
import spring.work.global.utils.EmailSender;
import spring.work.user.service.UserService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerHelperService {

    private final EmailSender emailSender;
    private final UserService userService;

    @RabbitListener(queues = "${rabbitmq.queue.mail}")
    public void signUpMailMessage(MailDto mailDto) {
        emailSender.sendEmail(mailDto);
    }

    @RabbitListener(queues = "${rabbitmq.queue.dlq.mail}")
    public void signUpMailErrorMessage(MailDto mailDto) {
        userService.sendMailFailHistory(mailDto);
    }
}
