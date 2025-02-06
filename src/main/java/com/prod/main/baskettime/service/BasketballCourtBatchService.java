package com.prod.main.baskettime.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.prod.main.baskettime.entity.BasketballCourt;
import com.prod.main.baskettime.repository.BasketballCourtRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class BasketballCourtBatchService {
    private static final List<double[]> CITIES_COORDINATES = List.of(
    new double[]{37.5665, 126.9780},  // 서울
    new double[]{35.1796, 129.0756},  // 부산
    new double[]{37.4563, 126.7052},  // 인천
    new double[]{35.8714, 128.6014},  // 대구
    new double[]{36.3504, 127.3845},  // 대전
    new double[]{35.1595, 126.8526},  // 광주
    new double[]{35.5384, 129.3114},  // 울산
    new double[]{33.4996, 126.5312}   // 제주
);

    
    private static final String KAKAO_MAPS_SEARCH_API = "https://dapi.kakao.com/v2/local/search/keyword.json";
    
    @Value("${kakao.api.key}") 
    private String kakaoApiKey;

    private final RestTemplate restTemplate;
    private final BasketballCourtRepository courtRepository;

    public BasketballCourtBatchService(RestTemplate restTemplate, BasketballCourtRepository courtRepository) {
        this.restTemplate = restTemplate;
        this.courtRepository = courtRepository;
    }

    // 배치 스케줄러: 전국 농구장 데이터 수집 및 저장
    //@Scheduled(cron = "0 0 3 * * ?")  // 매일 새벽 3시에 실행
    public void saveAllBasketballCourts() {
        List<BasketballCourt> allCourts = new ArrayList<>();

        for (double[] city : CITIES_COORDINATES) {
            double lat = city[0];
            double lng = city[1];

            List<BasketballCourt> courts = fetchBasketballCourts(lat, lng, 20000); // 5km 반경 검색
            allCourts.addAll(courts);
        }

        if (!allCourts.isEmpty()) {
            courtRepository.saveAll(allCourts);
        }
    }

    // 특정 지역 반경 내 농구장 검색
    private List<BasketballCourt> fetchBasketballCourts(double lat, double lng, int radius) {
        //String encodedKeyword = URLEncoder.encode("농구장", StandardCharsets.UTF_8);
        //System.out.println(encodedKeyword);
        String url = UriComponentsBuilder.fromHttpUrl(KAKAO_MAPS_SEARCH_API)
                .queryParam("query", "농구장")
                .queryParam("x", lng)
                .queryParam("y", lat)
                .queryParam("radius", radius)
                .queryParam("size", 15) // 한 번에 최대 15개 가져오기
                .encode()
                .toUriString();
        System.out.println(url);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> responseBody = response.getBody();
            
            if (responseBody != null) {
                // 🔹 응답 전체 확인
                System.out.println("카카오 API 응답: " + responseBody);

                // 🔹 meta 정보 확인
                Map<String, Object> meta = (Map<String, Object>) responseBody.get("meta");
                System.out.println("meta 정보: " + meta);

                // 🔹 documents 가져오기
                List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");
                System.out.println("검색된 농구장 개수: " + (documents != null ? documents.size() : 0));

            }
        }



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

                // 중복 저장 방지
                Optional<BasketballCourt> existingCourt = courtRepository.findByPlaceName(placeName);
                if (existingCourt.isEmpty()) {
                    BasketballCourt court = new BasketballCourt();
                    court.setPlaceName(placeName);
                    court.setAddress(address);
                    court.setRoadAddress(roadAddress);
                    court.setLatitude(latitude);
                    court.setLongitude(longitude);
                    court.setPhone(phone);
                    court.setCourtType("outdoor"); 
                    court.setIsPublic(true); 
                    court.setExtraInfo(""); 

                    courtsToSave.add(court);
                }
            }

            return courtsToSave;
        }
        return List.of();
    }
}

