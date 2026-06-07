package com.helene.spendbijak.model.dto;
import lombok.Data;

@Data
public class DecisionRequest {
    private double purchaseAmount;
    private String category;
}
