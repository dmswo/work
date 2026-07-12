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
                        너는 Work 프로젝트의 고객의 CS 문의를 담당하는 담당자이다.

                        회원가입한 사용자에게 보낼 환영 메일을 작성한다.

                        규칙
                        - 제목은 "프로젝트(Work) 회원가입을 진심으로 축하드립니다."라고 해줘
                        - 본문은 150자 이하
                        - 존댓말 사용
                        - 이모지 사용 금지
                        - 친근하지만 과장되지 않은 말투
                        - 반드시 JSON 형식으로 응답
                        - 본문은 문단마다 줄바꿈(\\n)을 사용해서 작성해줘.
                        """)
                .user("""
                        회원 닉네임 : %s

                        위 회원에게 보낼 환영 메일을 작성해줘.
                        
                        다음 내용을 포함해서 자연스럽게 작성해줘.
                        - Work 프로젝트 가입 감사
                        - 고객센터 문의 가능
                        - 가입 후 한 달 무료 이용 안내
                        - 마지막에 "Work 프로젝트 팀 드림"
                        
                        참고 예시(문장을 그대로 복사하지 말고 자연스럽게 작성)
                        
                        [프로젝트(Work)] %s님
                        프로젝트(Work)에 관심을 갖고 가입해주셔서 진심으로 감사드립니다.
                        앞으로 저희 프로젝트(Work)를 이용하시면서 불편한점이라던지 추가하고 싶은 기능이 있으시면 고객센터를 통해 문의 부탁드리며,
                        가입 한달간은 저희가 제공하는 서비스가 무료로 진행되는 점을 설명드립니다.
                        
                        프로젝트(Work) 팀 드림
                        
                        """.formatted(event.getNickname(), event.getNickname()))
                .call()
                .entity(MailContent.class);
    }
}
