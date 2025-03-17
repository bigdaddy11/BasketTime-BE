package com.prod.main.baskettime.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.Draw;
import com.prod.main.baskettime.service.DrawService;

@RestController
@RequestMapping("/api/draw")
public class DrawController {
     @Autowired
    private DrawService drawService;

    @GetMapping
    public ResponseEntity<List<Draw>> getDrawsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Draw> draws = drawService.getDrawsByDate(date);
        return ResponseEntity.ok(draws);
    }

    @GetMapping("/week")
    public ResponseEntity<List<Draw>> getDrawsForWeek() {
        List<Draw> drawList = drawService.getDrawsForWeek();
        return ResponseEntity.ok(drawList);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> saveBulkDraws(@RequestBody List<Draw> drawRequests) {
        List<Draw> savedDraws = drawRequests.stream()
                .map(drawService::saveOrUpdateDraw) // 중복 체크 후 저장
                .filter(draw -> draw != null) // 중복된 데이터 제외
                .collect(Collectors.toList());
    
        return ResponseEntity.ok(savedDraws);
    }
}
