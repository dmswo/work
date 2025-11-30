package spring.work.global.kafka.service;

import spring.work.global.kafka.dto.MailDto;

public interface ProducerHelperService {
    void sendMail(MailDto messageDto);
}
