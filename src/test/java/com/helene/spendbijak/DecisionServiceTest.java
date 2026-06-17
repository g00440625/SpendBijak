package com.helene.spendbijak;

import com.helene.spendbijak.model.dto.DecisionRequest;
import com.helene.spendbijak.model.dto.DecisionResponse;
import com.helene.spendbijak.model.entity.Expense;
import com.helene.spendbijak.model.entity.User;
import com.helene.spendbijak.repository.ExpenseRepository;
import com.helene.spendbijak.repository.GoalRepository;
import com.helene.spendbijak.repository.UserRepository;
import com.helene.spendbijak.service.DecisionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DecisionServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private DecisionService decisionService;

    @Test
    void shouldReturnSafe_whenUserHasHighSavingsAndLowPurchase() {
        // set up fake data
        User user  = new User();
        user.setMonthlySalary(3000);
        user.setSavings(10000);

        Expense expense = new Expense();
        expense.setAmount(1000);

        // fake repository responses
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(expenseRepository.findByUser_Id(1L)).thenReturn(List.of(expense));
        Mockito.when(goalRepository.findByUser_Id(1L)).thenReturn(List.of());

        // run the method
        DecisionRequest request = new DecisionRequest();
        request.setPurchaseAmount(100);
        request.setCategory("entertainment");

        DecisionResponse response = decisionService.getDecision(1L, request);

        assertEquals("SAFE", response.getVerdict());
    }

    @Test
    void shouldReturnHighRisk_whenExpensesAndPurchaseExceedIncome() {
        // set up fake data
        User user  = new User();
        user.setMonthlySalary(2000);
        user.setSavings(200);

        Expense expense = new Expense();
        expense.setAmount(1800);

        // fake repository responses
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(expenseRepository.findByUser_Id(1L)).thenReturn(List.of(expense));
        Mockito.when(goalRepository.findByUser_Id(1L)).thenReturn(List.of());

        // run the method
        DecisionRequest request = new DecisionRequest();
        request.setPurchaseAmount(300);
        request.setCategory("entertainment");

        DecisionResponse response = decisionService.getDecision(1L, request);

        assertEquals("HIGH RISK", response.getVerdict());
    }

}
