package com.example.skooldio.repository;

import com.example.skooldio.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getByUsername() {
        Optional<User> result = userRepository.getByUsername("somchai");
        assertTrue(result.isPresent());
    }
}