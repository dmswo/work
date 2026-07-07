package spring.work.global.ai.service;

import spring.work.global.ai.dto.MailContent;
import spring.work.global.kafka.dto.MailEvent;

public interface AiService {
    MailContent createWelcomeMail(MailEvent event);
}
