package com.prod.main.baskettime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prod.main.baskettime.entity.BasketballCourt;
import com.prod.main.baskettime.service.BasketballCourtBatchService;
import com.prod.main.baskettime.service.BasketballCourtService;

import java.util.List;

@RestController
@RequestMapping("/api/basketball-courts")
public class BasketballCourtController {
    private final BasketballCourtBatchService batchService;

    public BasketballCourtController(BasketballCourtBatchService batchService) {
        this.batchService = batchService;
    }


    @GetMapping("/batch")
    public ResponseEntity<String> triggerBatch() {
        batchService.saveAllBasketballCourts();
        return ResponseEntity.ok("농구장 데이터 수집 및 저장이 완료되었습니다.");
    }
}
