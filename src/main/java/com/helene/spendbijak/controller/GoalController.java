package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.entity.Goal;
import com.helene.spendbijak.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return goalService.createGoal(goal);
    }

    @GetMapping("user/{userId}")
    public List<Goal> getAllGoals(@PathVariable Long userId) {
        return goalService.getGoalsByUser(userId);
    }

    @PutMapping("/{goalId}/progress")
    public Goal addProgress(@PathVariable Long goalId, @RequestBody double amount) {
        return goalService.addProgress(goalId, amount);
    }
}
