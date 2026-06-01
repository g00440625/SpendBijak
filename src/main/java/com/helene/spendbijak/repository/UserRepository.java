package com.helene.spendbijak.repository;

import com.helene.spendbijak.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {
}