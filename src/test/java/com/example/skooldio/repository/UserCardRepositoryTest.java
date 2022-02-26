package com.example.skooldio.repository;

import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserCardRepositoryTest {

    @Autowired
    private UserCardRepository userCardRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void listByUserId() {
        User user = userRepository.findById(1L).get();
        Optional<List<UserCard>> result = userCardRepository.listByUserId(user, Pageable.unpaged());
        assertTrue(result.isPresent());
    }

    @Test
    void countByUserId() {
        User user = userRepository.findById(1L).get();
        Integer count = userCardRepository.countByUserId(user);
        assertNotNull(count);
    }
}