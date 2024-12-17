package com.prod.main.baskettime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Team;
import com.prod.main.baskettime.repository.TeamRepository;

import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class NBATeamController {

    @Autowired
    private TeamRepository teamRepository;

    @GetMapping("/nba")
    public ResponseEntity<List<Team>> getNbaTeams() {
        List<Team> teams = teamRepository.findByType("N");
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/kbl")
    public ResponseEntity<List<Team>> getKblTeams() {
        List<Team> teams = teamRepository.findByType("K");
        return ResponseEntity.ok(teams);
    }
}
