package spring.work.global.rabbitmq.utils;

import spring.work.global.rabbitmq.dto.MessageDto;

public interface ProducerHelperService {
    void eventSendMessage(MessageDto messageDto);
    void productSendMessage(MessageDto messageDto);
    void ticketSendMessage(MessageDto messageDto);
    void sendMail(MessageDto messageDto);
}
