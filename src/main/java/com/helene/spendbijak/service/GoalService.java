package com.helene.spendbijak.service;

import com.helene.spendbijak.model.entity.Goal;
import com.helene.spendbijak.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {
    private final GoalRepository goalRepository;

    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public List<Goal> getGoalsByUser(Long userId) {
        return goalRepository.findByUser_Id(userId);
    }
    // add progress toward a goal
    public Goal addProgress(Long goalId, double amount) {
        Goal goal = goalRepository.findById(goalId).orElseThrow();
        goal.setCurrentAmount(goal.getCurrentAmount() + amount);
        return goalRepository.save(goal);
    }
}
