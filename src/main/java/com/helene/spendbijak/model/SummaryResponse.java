package com.helene.spendbijak.model;
import lombok.Data;

@Data
public class SummaryResponse {
    private double totalIncome;
    private double totalSpent;
    private double remaining;
    private double percentageUsed;
}
