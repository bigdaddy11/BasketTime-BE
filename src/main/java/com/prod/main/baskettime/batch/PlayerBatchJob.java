package com.prod.main.baskettime.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prod.main.baskettime.entity.Player;
import com.prod.main.baskettime.repository.PlayerRepository;

import jakarta.transaction.Transactional;

@Component
public class PlayerBatchJob {

    private final String API_URL = "https://api.balldontlie.io/v1/players";
    private final String API_KEY = "9416c152-516e-4433-83ae-cafbf7114631";  // 여기에 API_KEY 입력
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private PlayerRepository playerRepository;  // DB에 저장할 리포지토리

    // 매일 한번 실행되도록 스케줄링 (@Scheduled)
    @Scheduled(cron = "0 0 0 */1 * *")  // 매일 자정에 실행 (크론 표현식: 초, 분, 시, 일, 월, 요일)
    //@Scheduled(cron = "0 */1 * * * *")  // 매 분마다 실행 (크론 표현식: 초, 분, 시, 일, 월, 요일)
    @Transactional
    public void fetchAndStoreAllPlayers() {
        List<Player> allPlayers = new ArrayList<>();  // DB에 저장할 엔티티 리스트
        int currentPage = 1;
        int cursor = 0;
        int totalPages = 10;  // 기본값 설정

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // 페이징 처리하여 모든 데이터를 가져옴
            while (currentPage <= totalPages) {
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", API_KEY);

                // API 호출
                String url = API_URL + "?cursor=" + cursor + "&per_page=" + 100;
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

                if (response != null && response.getBody() != null) {
                    // 응답을 JSON으로 변환하여 처리
                    Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);

                    List<Map<String, Object>> players = (List<Map<String, Object>>) responseBody.get("data");
                    for (Map<String, Object> playerData : players) {
                        // Player 엔티티로 변환하고 리스트에 추가
                        Player player = new Player();
                        player.setId((Integer) playerData.get("id"));
                        player.setFirstName((String) playerData.get("first_name"));
                        player.setLastName((String) playerData.get("last_name"));

                        // 팀 정보 설정
                        if (playerData.get("team") != null) {
                            Map<String, Object> teamData = (Map<String, Object>) playerData.get("team");
                            if (teamData.get("id") != null) {
                                try {
                                    // 팀 ID가 문자열일 수 있으므로 Integer로 변환
                                    player.setTeamId(Integer.parseInt(teamData.get("id").toString()));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    // 변환 실패 시, 기본값을 설정하거나 오류 처리 로직 추가
                                }
                            }
                        }
                        //player.setTeamId((Integer) playerData.get("team").get("id"));
                        player.setWeight((String) playerData.get("weight"));
                        player.setHeight((String) playerData.get("height"));
                        player.setPosition((String) playerData.get("position"));
                        player.setJerseyNumber((String) playerData.get("jersey_number"));
                        player.setDraftYear((Integer) playerData.get("draft_year"));
                        player.setDraftRound((Integer) playerData.get("draft_round"));
                        player.setDraftNumber((Integer) playerData.get("draft_number"));
                        player.setCountry((String) playerData.get("country"));
                        player.setCollege((String) playerData.get("college"));
                        
                        allPlayers.add(player);
                    }

                    currentPage++;  // 다음 페이지로 이동
                    cursor = cursor + 100;
                }
            }

            // DB에 저장 (기존 데이터 삭제 후 다시 저장)
            playerRepository.deleteAll();
            playerRepository.saveAll(allPlayers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
