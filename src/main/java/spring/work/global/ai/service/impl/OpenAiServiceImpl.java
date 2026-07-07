package spring.work.global.ai.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import spring.work.global.ai.service.AiService;
import spring.work.global.ai.dto.MailContent;
import spring.work.global.kafka.dto.MailEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiServiceImpl implements AiService {

    private final ChatClient chatClient;

    @Override
    public MailContent createWelcomeMail(MailEvent event) {
        return chatClient.prompt()
                .system("""
                        너는 Work 프로젝트의 고객 경험 담당자이다.

                        회원가입한 사용자에게 보낼 환영 메일을 작성한다.

                        규칙
                        - 제목은 30자 이하
                        - 본문은 150자 이하
                        - 존댓말 사용
                        - 이모지 사용 금지
                        - 친근하지만 과장되지 않은 말투
                        - 반드시 JSON 형식으로 응답
                        """)
                .user("""
                        회원 아이디 : %s

                        위 회원에게 보낼 환영 메일을 작성해줘.
                        """.formatted(event.getUserId()))
                .call()
                .entity(MailContent.class);
    }
}
