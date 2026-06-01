package com.helene.spendbijak.controller;

import com.helene.spendbijak.model.User;
import com.helene.spendbijak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
 public class UserController {

    private final UserService userService;

    //Receives JSON, return saved user
    @PostMapping
    public User createUser(@RequestBody User user)
    {
       return userService.createUser(user);
    }

    //Reads id, return that user
    public  User getUser(@PathVariable Long id)
    {
        return userService.getUserById(id);
    }
}
