package com.prod.main.baskettime.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
