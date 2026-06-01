package com.helene.spendbijak.repository;

import com.helene.spendbijak.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser_Id(Long userId);
}