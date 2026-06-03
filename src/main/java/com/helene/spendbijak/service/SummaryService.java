package com.helene.spendbijak.service;
import com.helene.spendbijak.model.Expense;
import com.helene.spendbijak.model.SummaryResponse;
import com.helene.spendbijak.model.User;
import com.helene.spendbijak.repository.ExpenseRepository;
import com.helene.spendbijak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public SummaryResponse getSummary(Long userId) {
        //Get User
        User user = userRepository.findById(userId).orElseThrow();

        //Get Expense
        List<Expense> expenses = expenseRepository.findByUser_Id(userId);

        //Add up all expense
        double totalSpent = 0.0;
        for (Expense expense : expenses) {
            totalSpent += expense.getAmount();
        }

        //Calculate remaining and percentage
        double remaining = user.getMonthlySalary() - totalSpent;
        double percentageUsed = (totalSpent / user.getMonthlySalary()) * 100;

        //Build response
        SummaryResponse summary = new SummaryResponse();
        summary.setTotalIncome(user.getMonthlySalary());
        summary.setTotalSpent(totalSpent);
        summary.setRemaining(remaining);
        summary.setPercentageUsed(percentageUsed);

        return summary;
    }
}
