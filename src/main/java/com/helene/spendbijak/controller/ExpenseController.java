package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.Expense;
import com.helene.spendbijak.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    public Expense saveExpense(@RequestBody Expense expense) {
        return expenseService.saveExpense(expense);
    }

    @GetMapping
    public List<Expense> getExpenseByUser(@PathVariable Long userId) {
        return expenseService.getExpenseByUser(userId);
    }
}

