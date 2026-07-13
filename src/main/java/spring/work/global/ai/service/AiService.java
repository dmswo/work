package spring.work.global.ai.service;

import spring.work.global.ai.dto.MailContent;
import spring.work.global.ai.dto.PostValidationResult;
import spring.work.global.kafka.dto.MailEvent;
import spring.work.post.dto.request.CreatePost;

public interface AiService {
    MailContent createWelcomeMail(MailEvent event);
    PostValidationResult validatePost(CreatePost request);
}
