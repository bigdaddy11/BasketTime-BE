package com.prod.main.baskettime.controller;

import com.prod.main.baskettime.entity.Match;
import com.prod.main.baskettime.repository.MatchRepository;
import com.prod.main.baskettime.service.MatchService;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private static final Logger logger = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchService matchService;

    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable("id") Long id) {
        Optional<Match> match = matchService.getMatchById(id);
        return match.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Match>> getAllTeams(
        @RequestParam("courtId") String courtId,
        @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Match> matchs = matchService.getMatchByCourtId(courtId, date);
        return ResponseEntity.ok(matchs);
    }

    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match matchdata) {
        Match match = matchRepository.save(matchdata);
        return ResponseEntity.ok(match);
    }
}
