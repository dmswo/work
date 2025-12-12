package spring.work.global.kafka.service;

import spring.work.global.kafka.dto.MailDto;

public interface ConsumerHelperService {
    void sendMail(MailDto messageDto);
    void failSendMail(MailDto messageDto);

    void cdcPostUpdate(String message);
}
