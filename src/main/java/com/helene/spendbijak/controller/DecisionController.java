package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.dto.DecisionRequest;
import com.helene.spendbijak.model.dto.DecisionResponse;
import com.helene.spendbijak.service.DecisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/decision")
@RequiredArgsConstructor
public class DecisionController {
    private final DecisionService decisionService;

    @PostMapping("/user/{userId}")
    public DecisionResponse getDecision(@PathVariable Long userId, @RequestBody DecisionRequest request)
    {
        return decisionService.getDecision(userId, request);
    }
}
