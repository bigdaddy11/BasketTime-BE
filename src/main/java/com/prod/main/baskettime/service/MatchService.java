package com.prod.main.baskettime.service;

import com.prod.main.baskettime.entity.Match;
import com.prod.main.baskettime.repository.MatchRepository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    @Autowired
    private MatchRepository matchRepository;

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }
    
    public List<Match> getMatchByCourtId(String courtId, LocalDate date) {
        return matchRepository.findByCourtIdAndMatchDate(courtId, date, Sort.by(Sort.Direction.ASC, "matchDate"));
    }
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }
}
