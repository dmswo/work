package spring.work.global.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import spring.work.global.kafka.dto.MailDto;

public interface ConsumerHelperService {
    void sendMail(MailDto messageDto);
    void failSendMail(MailDto messageDto);

    void cdcPostUpdate(ConsumerRecord<String, String> record);
}
