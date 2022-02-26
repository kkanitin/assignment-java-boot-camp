package com.example.skooldio.repository;

import com.example.skooldio.entity.Basket;
import com.example.skooldio.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BasketRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BasketRepository basketRepository;

    @Test
    void listByUserId() {
        User user = userRepository.findById(1L).orElse(null);
        Optional<List<Basket>> result = basketRepository.listByUserId(user, Pageable.unpaged());

        assertTrue(result.isPresent());
    }

    @Test
    void updateQuantity() {
        basketRepository.updateQuantity(1L, 22);
        Basket basket = basketRepository.getById(1L);
        assertEquals(22, basket.getQuantity());
    }
}