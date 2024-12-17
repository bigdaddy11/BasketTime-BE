package com.prod.main.baskettime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Player;
import com.prod.main.baskettime.entity.Team;
import com.prod.main.baskettime.repository.PlayerRepository;
import com.prod.main.baskettime.repository.TeamRepository;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository
    ;
    // 저장 또는 업데이트 메서드
    public void saveOrUpdatePlayers(List<Player> players) {
        for (Player player : players) {
            // 1. 팀 이름으로 팀 ID 조회
            Team team = teamRepository.findByFullName(player.getTeamName());

            if (team != null) {
                player.setTeamId(team.getId()); // Team ID 세팅
            } else {
                System.out.println("팀 정보를 찾을 수 없습니다: " + player.getTeamName());
                continue; // 팀 정보가 없으면 다음 플레이어로 넘어감
            }

            // 2. 기존 플레이어 조회
            Player existingPlayer = playerRepository.findByFirstName(player.getFirstName());

            if (existingPlayer != null) {
                // 기존 데이터 업데이트
                existingPlayer.setTeamId(player.getTeamId());
                existingPlayer.setPosition(player.getPosition());
                existingPlayer.setJerseyNumber(player.getJerseyNumber());
                existingPlayer.setWeight(player.getWeight());
                existingPlayer.setHeight(player.getHeight());
                playerRepository.save(existingPlayer);
                System.out.println("업데이트된 선수: " + player.getFirstName() + " ");
            } else {
                // 새 데이터 삽입
                playerRepository.save(player);
                System.out.println("새로 저장된 선수: " + player.getFirstName() + " " + player.getLastName());
            }
        }
    }
}
