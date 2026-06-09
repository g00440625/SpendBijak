package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.entity.Budget;
import com.helene.spendbijak.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @PostMapping
    public Budget createBudget(@RequestBody Budget budget) {
        return budgetService.createBudget(budget);
    }

    @GetMapping("user/{userId}")
    public List<Budget> getAllBudgets(@PathVariable Long userId) {
        return budgetService.getBudgetByUserId(userId);
    }

}
