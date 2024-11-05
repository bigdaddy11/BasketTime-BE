package com.prod.main.baskettime.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/api/auth/google")
public class LoginController {

    private final DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver;

    public LoginController(ClientRegistrationRepository clientRegistrationRepository) {
        this.authorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(
                clientRegistrationRepository, "/oauth2/authorization");
    }

    @GetMapping("/google-url")
    public URI getGoogleAuthUrl(HttpServletRequest request) {
        System.out.println("google-url");
        OAuth2AuthorizationRequest authorizationRequest = authorizationRequestResolver.resolve(request, "google");
        return authorizationRequest != null ? URI.create(authorizationRequest.getAuthorizationRequestUri()) : null;
    }

    @GetMapping("/callback")
    public Map<String, Object> googleCallback(
        @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
        OAuth2User principal
    ) {
        System.out.println("여기는 들어왔니?");
        // 인증된 사용자 정보 및 액세스 토큰을 반환
        return Map.of(
            "name", principal.getAttribute("name"),
            "email", principal.getAttribute("email"),
            "accessToken", authorizedClient.getAccessToken().getTokenValue()
        );
    }

    @PostMapping("/google-token")
    public Map<String, Object> receiveTokenAndFetchUserInfo(@RequestBody Map<String, String> request) {
        String accessToken = request.get("token");

        // Google UserInfo API를 호출하여 사용자 정보 가져오기
        String userInfoEndpoint = "https://openidconnect.googleapis.com/v1/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> userInfo = restTemplate.getForObject(userInfoEndpoint + "?access_token=" + accessToken, Map.class);

        // 필요한 경우 사용자 정보를 저장하거나 처리
        return userInfo;  // 사용자 정보를 클라이언트에 반환
    }
}
