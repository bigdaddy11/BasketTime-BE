package com.prod.main.baskettime.batch;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.prod.main.baskettime.entity.PostComment;
import com.prod.main.baskettime.entity.PushToken;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.repository.PaperPlanRepository;
import com.prod.main.baskettime.repository.PostCommentRepository;
import com.prod.main.baskettime.repository.PushTokenRepository;
import com.prod.main.baskettime.service.GoogleAuthService;
import com.prod.main.baskettime.service.PostCommentService;
import com.prod.main.baskettime.service.PushTokenService;

@Component
public class PushNotificationScheduler {

    private final GoogleAuthService googleAuthService;
    private final PostCommentRepository postCommentRepository;
    private final PushTokenRepository pushTokenRepository;
    private final PaperPlanRepository paperPlanRepository;

    public PushNotificationScheduler(GoogleAuthService googleAuthService,
                                     PostCommentRepository postCommentRepository,
                                     PushTokenRepository pushTokenRepository,
                                     PaperPlanRepository paperPlanRepository) {
        this.googleAuthService = googleAuthService;
        this.postCommentRepository = postCommentRepository;
        this.pushTokenRepository = pushTokenRepository;
        this.paperPlanRepository = paperPlanRepository;
    }

    // ✅ 10분마다 실행 (600,000ms = 10분)
    //@Scheduled(fixedRate = 600000)
    public void sendNewCommentNotifications() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        // 10분 이내에 등록된 새로운 댓글 조회
        List<Object[]> recentComments = postCommentRepository.findRecentCommentsWithAuthor(tenMinutesAgo);

        if (recentComments.isEmpty()) {
            return; // ✅ 새로운 댓글이 없으면 푸쉬 전송하지 않음
        }

        // ✅ 작성자별 댓글 개수 계산
        Map<Long, Long> commentCountByUserId = recentComments.stream()
                .collect(Collectors.groupingBy(row -> (Long) row[4], Collectors.counting()));

        // 작성자에게 푸쉬 전송
        for (Map.Entry<Long, Long> entry : commentCountByUserId.entrySet()) {
            Long userId = entry.getKey();
            Long commentCount = entry.getValue();

            List<PushToken> pushTokens = pushTokenRepository.findByUserId(userId);
            if (!pushTokens.isEmpty()) {
                String title = "새로운 댓글 알림";
                String body = commentCount + "개의 새로운 댓글이 달렸습니다.";

                for (PushToken token : pushTokens) {
                    googleAuthService.sendPushNotification(token.getToken(), title, body);
                }
            }
        }
    }

    //@Scheduled(fixedRate = 600000) // 10분마다 실행
    public void sendNewMessageNotifications() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        // 10분 이내에 등록된 새로운 쪽지 조회
        List<Object[]> recentMessages = paperPlanRepository.findRecentMessages(tenMinutesAgo);

        if (recentMessages.isEmpty()) {
            return; // ✅ 새로운 쪽지가 없으면 푸쉬 전송하지 않음
        }

        // ✅ 받은 사람별 쪽지 개수 계산
        Map<Long, Long> messageCountByUserId = recentMessages.stream()
                .collect(Collectors.groupingBy(row -> (Long) row[1], Collectors.counting()));

        // 받은 사용자에게 푸쉬 전송
        for (Map.Entry<Long, Long> entry : messageCountByUserId.entrySet()) {
            Long userId = entry.getKey();
            Long messageCount = entry.getValue();

            List<PushToken> pushTokens = pushTokenRepository.findByUserId(userId);
            if (!pushTokens.isEmpty()) {
                String title = "새로운 쪽지 도착";
                String body = messageCount + "개의 새로운 쪽지가 도착했습니다.";

                for (PushToken token : pushTokens) {
                    googleAuthService.sendPushNotification(token.getToken(), title, body);
                }
            }
        }
    }
}