// package com.prod.main.baskettime.batch;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestTemplate;

// @Component
// public class BatchScheduler {
//     private final RestTemplate restTemplate = new RestTemplate();

//     @Value("${base.url}")
//     private String baseUrl;

//     @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
//     //@Scheduled(cron = "0 12 13 * * ?") // 매일 새벽 1시에 실행
//     public void runNikeBatch() {
//         executeBatch(baseUrl + "/api/draw/nike", "Nike Batch");
//     }

//     @Scheduled(cron = "0 5 1 * * ?") // 매일 새벽 1시 5분에 실행
//     //@Scheduled(cron = "0 41 11 * * ?") // 매일 새벽 1시에 실행
//     public void runNewBalanceBatch() {
//         executeBatch(baseUrl + "/api/draw/newbalance", "New Balance Batch");
//     }

//     @Scheduled(cron = "0 10 1 * * ?") // 매일 새벽 1시 10분에 실행
//     //@Scheduled(cron = "0 42 11 * * ?") // 매일 새벽 1시에 실행
//     public void runNewBalanceKidsBatch() {
//         executeBatch(baseUrl + "/api/draw/newbalance/kids", "New Balance Kids Batch");
//     }

//     @Scheduled(cron = "0 15 1 * * ?") // 매일 새벽 1시 15분에 실행
//     //@Scheduled(cron = "0 43 11 * * ?") // 매일 새벽 1시에 실행
//     public void runAsicsBatch() {
//         executeBatch(baseUrl + "/api/draw/asics", "Asics Batch");
//     }

//     private void executeBatch(String url, String batchName) {
//         try {
//             System.out.println(batchName + " 시작");
//             String response = restTemplate.getForObject(url, String.class);
//             System.out.println(batchName + " 완료: " + response);
//         } catch (Exception e) {
//             System.err.println(batchName + " 실패: " + e.getMessage());
//         }
//     }
// }
