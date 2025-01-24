package com.prod.main.baskettime.batch;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prod.main.baskettime.entity.Player;
import com.prod.main.baskettime.entity.Team;
import com.prod.main.baskettime.repository.PlayerRepository;
import com.prod.main.baskettime.repository.TeamRepository;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/nba/team/batch")
public class NBATeamBatchJob {
    private final String API_URL = "https://api.balldontlie.io/v1/teams";
    private final String API_KEY = "9416c152-516e-4433-83ae-cafbf7114631";
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TeamRepository teamRepository;  // JPA 리포지토리

    
    //@Scheduled(cron = "0 5 0 * * *") // 매일 0시 5분에 실행
    //@Scheduled(cron = "0 19 * * * *")  // 매 분마다 실행 (크론 표현식: 초, 분, 시, 일, 월, 요일)
    @GetMapping
    public void fetchAndStoreAllTeams() {
        List<Team> allTeams = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("스케줄러 동작 시작: " + LocalDateTime.now());
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", API_KEY);

            // API 호출
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

            if (response != null && response.getBody() != null) {
                // 응답을 JSON으로 변환하여 처리
                Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);

                List<Map<String, Object>> teams = (List<Map<String, Object>>) responseBody.get("data");
                for (Map<String, Object> teamData : teams) {
                    // Team 엔티티로 변환하고 리스트에 추가
                    Team team = new Team();
                    team.setId((Integer) teamData.get("id"));
                    team.setConference((String) teamData.get("conference"));
                    team.setDivision((String) teamData.get("division"));
                    team.setCity((String) teamData.get("city"));
                    team.setName((String) teamData.get("name"));
                    team.setFullName((String) teamData.get("full_name"));
                    team.setAbbreviation((String) teamData.get("abbreviation"));
                    team.setType("N");
                    allTeams.add(team);
                }

                // DB에 저장 (기존 데이터 삭제 후 다시 저장)
                //teamRepository.deleteAll();
                teamRepository.saveAll(allTeams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
