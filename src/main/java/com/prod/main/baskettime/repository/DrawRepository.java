package com.prod.main.baskettime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Draw> findByProductCodeIsNotNull();

    // 현재일 기준으로 일주일 간의 드로우 데이터 가져오기
    @Query("SELECT d FROM Draw d WHERE d.drawDate BETWEEN :startDate AND :endDate ORDER BY d.drawDate ASC")
    List<Draw> findDrawsForWeek(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}