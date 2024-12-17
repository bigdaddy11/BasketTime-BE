package com.prod.main.baskettime.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prod.main.baskettime.entity.Team;
import com.prod.main.baskettime.repository.TeamRepository;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    private Integer nextId = 1000; // ID 초기값 1000

    // 저장 또는 업데이트 메서드
    public void saveOrUpdateTeams(List<Team> teams) {
        for (Team newTeam : teams) {
            Team existingTeam = teamRepository.findByFullName(newTeam.getFullName());

            if (existingTeam != null) {
                // 변경사항 비교
                if (isTeamChanged(existingTeam, newTeam)) {
                    existingTeam.setType(newTeam.getType()); // 필요 시 다른 필드 업데이트
                    teamRepository.save(existingTeam);
                    System.out.println("업데이트된 팀: " + existingTeam.getFullName());
                } else {
                    System.out.println("변경사항 없음: " + existingTeam.getFullName());
                }
            } else {
                // 기존 데이터가 없으면 삽입
                newTeam.setId(nextId++);
                teamRepository.save(newTeam);
                System.out.println("새로 저장된 팀: " + newTeam.getFullName());
            }
        }
    }

    // 변경사항 비교 메서드
    private boolean isTeamChanged(Team existing, Team updated) {
        return !existing.getType().equals(updated.getType());
    }
}
