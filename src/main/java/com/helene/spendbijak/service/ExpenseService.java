package com.helene.spendbijak.service;

import com.helene.spendbijak.model.Expense;
import com.helene.spendbijak.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public Expense saveExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpenseByUser(Long userId) {
        return expenseRepository.findByUser_Id(userId);
    }
}
