package com.helene.spendbijak.service;

import com.helene.spendbijak.model.dto.DecisionRequest;
import com.helene.spendbijak.model.dto.DecisionResponse;
import com.helene.spendbijak.model.entity.Expense;
import com.helene.spendbijak.model.entity.User;
import com.helene.spendbijak.repository.ExpenseRepository;
import com.helene.spendbijak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DecisionService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public DecisionResponse  getDecision(Long userId,DecisionRequest request) {


        User user = userRepository.findById(userId).orElseThrow();
        List<Expense> expenses = expenseRepository.findByUser_Id(userId);

        // calculate total expense
        double totalSpent = 0.0;
        for (Expense expense : expenses) {
            totalSpent += expense.getAmount();
        }

        // Calculate Score
        // emergency fund score
        double monthlyExpenses = totalSpent;
        double emergencyMonths = user.getSavings() / monthlyExpenses;

        double emergencyFundScore;
        if (emergencyMonths >=6) {
            emergencyFundScore = 0; // safe
        }
        else if (emergencyMonths >=3) {
            emergencyFundScore = 0.3; // okay
        }
        else if (emergencyMonths >=1) {
            emergencyFundScore = 0.7; // risky
        }
        else {
            emergencyFundScore = 1; // dangerous
        }

        // affordability score based on 50/30/20 rule
        double purchaseToIncome = request.getPurchaseAmount()/user.getMonthlySalary();

        double affordabilityScore;
        if (purchaseToIncome <= 0.10) {
            affordabilityScore = 0.0; // okay
        }
        else if (purchaseToIncome <= 0.20) {
            affordabilityScore = 0.2; // low risk
        }
        else if (purchaseToIncome <= 0.35) {
            affordabilityScore = 0.5; // moderate
        }
        else if (purchaseToIncome <= 0.50) {
            affordabilityScore = 0.7; // risky
        }
        else {
            affordabilityScore = 1; // dangerous
        }

        // savings rate score
        double disposable = user.getMonthlySalary() - monthlyExpenses;
        double savingsRate = disposable / user.getMonthlySalary();

        double savingsRateScore;
        if (savingsRate >= 0.20) {
            savingsRateScore = 0.0; // healthy
        } else if (savingsRate >= 0.10) {
            savingsRateScore = 0.3; // moderate
        } else if (savingsRate >= 0.05) {
            savingsRateScore = 0.7; // low
        } else {
            savingsRateScore = 1; // very low
        }
        double debtScore = 0.0;
        double goalImpactScore = 0.0;

        // full risk calculation
        double riskScore = (emergencyFundScore  * 0.30)
                + (affordabilityScore  * 0.25)
                + (savingsRateScore    * 0.20)
                + (debtScore           * 0.15)
                + (goalImpactScore     * 0.10);

        // convert to percentage
        double riskPercentage = riskScore * 100;

        // verdict
        String verdict;
        if (riskPercentage < 30) {
            verdict = "SAFE";
        }
        else if (riskPercentage < 60) {
            verdict = "MODERATE";
        }
        else                        {
            verdict = "HIGH RISK";
        }

        // decision reason
        double remainingAfter = user.getMonthlySalary() - totalSpent - request.getPurchaseAmount();
        String reason = String.format(
                "Your emergency fund covers %.1f months of expenses " +
                        "(recommended: 3-6 months). " +
                        "This purchase is %.0f%% of your monthly income. " +
                        "After this purchase you will have €%.2f remaining. " +
                        "Risk score: %.0f/100.",
                emergencyMonths,
                purchaseToIncome * 100,
                remainingAfter,
                riskPercentage
        );

        DecisionResponse response = new DecisionResponse();
        response.setReason(reason);
        response.setVerdict(verdict);
        response.setRemainingAfter(remainingAfter);
        response.setRiskScore(riskPercentage);
        return response;
    }
}
