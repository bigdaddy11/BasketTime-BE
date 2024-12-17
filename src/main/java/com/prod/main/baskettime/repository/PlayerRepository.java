package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Player;
import com.prod.main.baskettime.entity.Team;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByFirstName(String firstName);

    // Type 조건으로 팀 조회
    List<Player> findByType(String type);
}
