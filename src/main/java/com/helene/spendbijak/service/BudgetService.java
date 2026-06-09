package com.helene.spendbijak.service;

import com.helene.spendbijak.model.entity.Budget;
import com.helene.spendbijak.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    // save new budget for a category
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    //get all budget limits for a user
    public List<Budget> getBudgetByUserId(Long userId) {
        return budgetRepository.findByUser_Id(userId);
    }
}
