package com.helene.spendbijak.model.dto;
import lombok.Data;

@Data
public class DecisionResponse {
    private String verdict;
    private String reason;
    private double riskScore;
    private double remainingAfter;
}
