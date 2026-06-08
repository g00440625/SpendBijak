package com.helene.spendbijak.repository;

import com.helene.spendbijak.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {
}