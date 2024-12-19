package com.prod.main.baskettime.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.prod.main.baskettime.entity.Player;
import com.prod.main.baskettime.repository.PlayerRepository;
import com.prod.main.baskettime.service.PlayerService;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/nba")
    public ResponseEntity<List<Player>> getNbaPlayers() {
        List<Player> players = playerRepository.findByType("N");
        return ResponseEntity.ok(players);
    }

    @GetMapping("/kbl")
    public ResponseEntity<List<Player>> getkblPlayers() {
        List<Player> players = playerRepository.findByType("K");
        return ResponseEntity.ok(players);
    }
}
