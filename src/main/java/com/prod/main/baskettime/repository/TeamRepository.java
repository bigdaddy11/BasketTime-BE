package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prod.main.baskettime.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
