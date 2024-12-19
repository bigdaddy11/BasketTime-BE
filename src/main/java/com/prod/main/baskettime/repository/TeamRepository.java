package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByFullName(String fullName);

    Team findByAbbreviation(String abbreviation);

    // Type 조건으로 팀 조회
    List<Team> findByType(String type);
}
