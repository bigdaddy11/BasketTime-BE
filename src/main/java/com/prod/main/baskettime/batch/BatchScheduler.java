package com.prod.main.baskettime.batch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BatchScheduler {
    @Value("${server.name:primary}") // 서버 이름을 시스템 프로퍼티에서 가져옴
    private String serverName;

    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    //@Scheduled(cron = "0 40 11 * * ?") // 매일 새벽 1시에 실행
    public void runNikeBatch() {
        executeBatch("http://localhost:8080/api/draw/nike", "Nike Batch");
    }

    @Scheduled(cron = "0 5 1 * * ?") // 매일 새벽 1시 5분에 실행
    //@Scheduled(cron = "0 41 11 * * ?") // 매일 새벽 1시에 실행
    public void runNewBalanceBatch() {
        executeBatch("http://localhost:8080/api/draw/newbalance", "New Balance Batch");
    }

    @Scheduled(cron = "0 10 1 * * ?") // 매일 새벽 1시 10분에 실행
    //@Scheduled(cron = "0 42 11 * * ?") // 매일 새벽 1시에 실행
    public void runNewBalanceKidsBatch() {
        executeBatch("http://localhost:8080/api/draw/newbalance/kids", "New Balance Kids Batch");
    }

    @Scheduled(cron = "0 15 1 * * ?") // 매일 새벽 1시 15분에 실행
    //@Scheduled(cron = "0 43 11 * * ?") // 매일 새벽 1시에 실행
    public void runAsicsBatch() {
        executeBatch("http://localhost:8080/api/draw/asics", "Asics Batch");
    }

    private void executeBatch(String url, String batchName) {
        if ("primary".equals(serverName)) {
            try {
                System.out.println(batchName + " 시작");
                String response = restTemplate.getForObject(url, String.class);
                System.out.println(batchName + " 완료: " + response);
            } catch (Exception e) {
                System.err.println(batchName + " 실패: " + e.getMessage());
            }
        } else {
            System.out.println(batchName + " 스킵: 현재 서버는 primary가 아님");
        }
    }
}
