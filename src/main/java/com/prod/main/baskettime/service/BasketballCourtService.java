package com.prod.main.baskettime.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.prod.main.baskettime.entity.BasketballCourt;
import com.prod.main.baskettime.repository.BasketballCourtRepository;

import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BasketballCourtService {
    
    private static final String KAKAO_MAPS_SEARCH_API = "https://dapi.kakao.com/v2/local/search/keyword.json";

    @Value("${kakao.api.key}")  // ✅ application.yml에서 값 가져오기
    private String kakaoApiKey;

    private final RestTemplate restTemplate;
    private final BasketballCourtRepository courtRepository;

    public BasketballCourtService(RestTemplate restTemplate, BasketballCourtRepository courtRepository) {
        this.restTemplate = restTemplate;
        this.courtRepository = courtRepository;
    }

    // ✅ 농구장 검색 (카카오 API → 바로 엔티티 변환)
    public List<BasketballCourt> searchAndSaveBasketballCourts(double lat, double lng, int radius) {
        String url = UriComponentsBuilder.fromHttpUrl(KAKAO_MAPS_SEARCH_API)
                .queryParam("query", "농구장")  
                .queryParam("x", lng)  
                .queryParam("y", lat)  
                .queryParam("radius", radius)  
                .queryParam("size", 10)  
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);  // ✅ API 키 설정

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<Map<String, Object>> documents = (List<Map<String, Object>>) response.getBody().get("documents");

            List<BasketballCourt> courtsToSave = new ArrayList<>();

            for (Map<String, Object> doc : documents) {
                String placeName = (String) doc.get("place_name");
                String address = (String) doc.get("address_name");
                String roadAddress = (String) doc.get("road_address_name");
                String phone = (String) doc.get("phone");
                double latitude = Double.parseDouble((String) doc.get("y"));
                double longitude = Double.parseDouble((String) doc.get("x"));

                // 중복 확인 (같은 이름의 농구장이 있는지)
                Optional<BasketballCourt> existingCourt = courtRepository.findByPlaceName(placeName);
                if (existingCourt.isEmpty()) {
                    BasketballCourt court = new BasketballCourt();
                    court.setPlaceName(placeName);
                    court.setAddress(address);
                    court.setRoadAddress(roadAddress);
                    court.setLatitude(latitude);
                    court.setLongitude(longitude);
                    court.setPhone(phone);
                    court.setCourtType("outdoor"); // 기본값
                    court.setIsPublic(true); // 기본값
                    court.setExtraInfo(""); // 추가 정보 기본값

                    courtsToSave.add(court);
                }
            }

            if (!courtsToSave.isEmpty()) {
                courtRepository.saveAll(courtsToSave);
            }

            return courtsToSave;
        }
        return List.of();
    }

    public void saveCourts(List<BasketballCourt> courts) {
        courtRepository.saveAll(courts);
    }
}

