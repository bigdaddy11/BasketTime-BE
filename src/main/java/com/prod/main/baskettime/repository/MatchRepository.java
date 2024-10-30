package com.prod.main.baskettime.repository;

import java.util.List;
import java.time.LocalDate;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prod.main.baskettime.entity.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m WHERE m.courtId = :courtId AND FUNCTION('DATE', m.matchDate) = :date")
    List<Match> findByCourtIdAndMatchDate(@Param("courtId") String courtId, @Param("date") LocalDate date, Sort sort);
}
