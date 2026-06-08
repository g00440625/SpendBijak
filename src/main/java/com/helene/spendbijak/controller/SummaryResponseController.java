package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.dto.SummaryResponse;
import com.helene.spendbijak.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryResponseController {
    private final SummaryService summaryService;

    @GetMapping("/user/{userId}")
    public SummaryResponse getSummary(@PathVariable Long userId) {
        return summaryService.getSummary(userId);
    }
}
