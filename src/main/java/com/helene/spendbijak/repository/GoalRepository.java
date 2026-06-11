package com.helene.spendbijak.repository;

import com.helene.spendbijak.model.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser_Id(Long userId);
}
