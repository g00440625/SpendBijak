package com.helene.spendbijak.service;

import com.helene.spendbijak.config.RiskWeights;
import com.helene.spendbijak.model.dto.DecisionRequest;
import com.helene.spendbijak.model.dto.DecisionResponse;
import com.helene.spendbijak.model.entity.Expense;
import com.helene.spendbijak.model.entity.Goal;
import com.helene.spendbijak.model.entity.User;
import com.helene.spendbijak.repository.ExpenseRepository;
import com.helene.spendbijak.repository.GoalRepository;
import com.helene.spendbijak.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DecisionService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final GoalRepository goalRepository;

    public DecisionResponse  getDecision(Long userId,DecisionRequest request) {

        User user = userRepository.findById(userId).orElseThrow();
        List<Expense> expenses = expenseRepository.findByUser_Id(userId);
        List<Goal> goal = goalRepository.findByUser_Id(userId);

        // calculate total expense
        double totalSpent = 0.0;
        for (Expense expense : expenses) {
            totalSpent += expense.getAmount();
        }

        // Calculate Score
        // emergency fund score
        double monthlyExpenses = totalSpent;
        double emergencyMonths = (monthlyExpenses > 0)
                ? user.getSavings() / monthlyExpenses
                : 6.0; // assume safe if no expenses yet


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

        double remainingIncome = user.getMonthlySalary() - monthlyExpenses;

        // affordability score based on 50/30/20 rule
        double purchaseToRemainingIncome = (remainingIncome > 0)
                ? request.getPurchaseAmount() / remainingIncome
                : 1.0; // maximum risk if no income remaining

        double affordabilityScore;
        if (purchaseToRemainingIncome <= 0.10) {
            affordabilityScore = 0.0; // okay
        }
        else if (purchaseToRemainingIncome <= 0.20) {
            affordabilityScore = 0.2; // low risk
        }
        else if (purchaseToRemainingIncome <= 0.35) {
            affordabilityScore = 0.5; // moderate
        }
        else if (purchaseToRemainingIncome <= 0.50) {
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

        // calculate goal impact score to requested purchase
        double goalImpactScore = 0.0;
        if (!goal.isEmpty()){
           Goal mainGoal = goal.getFirst();

           double remaining = mainGoal.getTargetAmount() - mainGoal.getCurrentAmount();

           // how many months to reach the goal
           long monthsLeft = ChronoUnit.MONTHS.between(LocalDate.now(), mainGoal.getTargetDate());

           // required monthly savings
           double requiredMonthly = remaining / monthsLeft;

           // money user has left after purchase
           double disposableAfterPurchase = user.getMonthlySalary() - monthlyExpenses - request.getPurchaseAmount();

           if(disposableAfterPurchase < requiredMonthly){
               goalImpactScore = 1.0 - (disposableAfterPurchase / requiredMonthly);

               // keep the range within 0 and 1
               goalImpactScore = Math.clamp(goalImpactScore, 0.0, 1.0);
           }
        }

        // full risk calculation
        double riskScore = (emergencyFundScore  * RiskWeights.EMERGENCY_FUND_WEIGHT)
                + (affordabilityScore  * RiskWeights.AFFORDABILITY_WEIGHT)
                + (savingsRateScore    * RiskWeights.SAVINGS_WEIGHT)
                + (goalImpactScore     * RiskWeights.GOALS_WEIGHT);

        // calculate only active weights
        double activeWeights = 0;

        activeWeights += RiskWeights.EMERGENCY_FUND_WEIGHT;
        activeWeights += RiskWeights.AFFORDABILITY_WEIGHT;
        activeWeights += RiskWeights.SAVINGS_WEIGHT;

        boolean hasGoalData = !goal.isEmpty();

        if (hasGoalData) {
            activeWeights += RiskWeights.GOALS_WEIGHT;
        }

        double normalizedScore = riskScore / activeWeights;
        double riskPercentage = normalizedScore * 100;

        // verdict
        String verdict;
        if (riskPercentage < 30) {
            verdict = "SAFE";
        }
        else if (riskPercentage < 60) {
            verdict = "MODERATE";
        }
        else {
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
                purchaseToRemainingIncome * 100,
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
