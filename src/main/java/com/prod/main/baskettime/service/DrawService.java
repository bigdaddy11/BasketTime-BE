package com.prod.main.baskettime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prod.main.baskettime.entity.Draw;
import com.prod.main.baskettime.repository.DrawRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DrawService {
    @Autowired
    private DrawRepository drawRepository;

    @Transactional
    public Draw saveOrUpdateDraw(Draw draw) {
        // 동일 날짜 및 이름 존재 여부 확인
        List<Draw> existingDraws = drawRepository.findByDrawNameAndDrawDateAndProductCodeAndDrawStyle(draw.getDrawName(), draw.getDrawDate(), draw.getProductCode(), draw.getDrawStyle());
        if (!existingDraws.isEmpty()) {
            System.out.println("이미 존재하는 응모: " + draw.getDrawName() + ", 날짜: " + draw.getDrawDate());
            return null; // 저장하지 않음
        }
        return drawRepository.save(draw); // 새로운 데이터 저장
    }

    public List<Draw> getAllDraws() {
        return drawRepository.findAll();
    }

    public List<Draw> getDrawsByDate(LocalDate date) {
        return drawRepository.findByDrawDate(date);
    }

    public List<Draw> findAllWithProductCode() {
        return drawRepository.findByProductCodeIsNotNull();
    }

    /**
     * 현재 날짜 기준으로 일주일 간의 드로우 데이터 조회
     */
    public List<Draw> getDrawsForWeek() {
        LocalDate today = LocalDate.now();
        LocalDate oneWeekLater = today.plusDays(7);
        return drawRepository.findDrawsForWeek(today, oneWeekLater);
    }
}
