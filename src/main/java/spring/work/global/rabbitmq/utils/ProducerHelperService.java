package spring.work.global.rabbitmq.utils;

import spring.work.global.rabbitmq.dto.MailDto;

public interface ProducerHelperService {
    void sendMail(MailDto messageDto);
}
