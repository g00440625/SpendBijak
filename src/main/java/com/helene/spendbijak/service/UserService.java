package com.helene.spendbijak.service;

import com.helene.spendbijak.model.entity.User;
import com.helene.spendbijak.repository.UserRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //Saves a new user to the database
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     * Returns null if user not found.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElse(null);
    }
}
