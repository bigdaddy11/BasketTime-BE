package com.prod.main.baskettime.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// Google Cloud Translation API 관련 임포트
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

@RestController
@RequestMapping("/api/news")
public class NBANewsTranslateController {

    private final String GOOGLE_NEWS_API_URL = "https://newsapi.org/v2/everything";
    private final String NEWS_API_KEY = "187d1eafc15e4ec385832a213984a97b"; // 뉴스 API 키 필요
    private final String TRANSLATE_API_KEY = "AIzaSyCiP64B8lfsCRHNDa94JRJJ0JI1qd8kXuQ"; // 번역 API 키 필요

    @GetMapping("/nba/korean")
    public ResponseEntity<String> getTranslatedNBANews(@RequestParam(value = "query", required = false, defaultValue = "NBA") String query) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            // 1. 뉴스 API 호출
            String url = GOOGLE_NEWS_API_URL + "?q=" + query + "&apiKey=" + NEWS_API_KEY;
            String newsResponse = restTemplate.getForObject(url, String.class);

            // 2. 뉴스 데이터를 한국어로 번역
            Translate translate = TranslateOptions.newBuilder().setApiKey(TRANSLATE_API_KEY).build().getService();
            Translation translation = translate.translate(newsResponse, Translate.TranslateOption.targetLanguage("ko"));

            // 3. 번역된 데이터 반환
            return new ResponseEntity<>(translation.getTranslatedText(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching and translating NBA news data", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
