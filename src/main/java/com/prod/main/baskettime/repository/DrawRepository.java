package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prod.main.baskettime.entity.Draw;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {
    // 동일한 이름과 날짜가 있는 데이터 검색
    List<Draw> findByDrawNameAndDrawDateAndProductCodeAndDrawStyle(String drawName, LocalDate drawDate, String productCode, String drawStyle );

    List<Draw> findByDrawNameAndDrawDateAndDrawStyle(String drawName, LocalDate drawDate, String drawStyle);

    List<Draw> findByDrawDate(LocalDate drawDate);
}