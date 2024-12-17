package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    Player findByFirstName(String firstName);
}
