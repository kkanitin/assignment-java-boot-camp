package com.example.skooldio.repository;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void listByUserId() {
        User user = userRepository.findById(1L).orElse(null);
        Optional<List<Address>> result = addressRepository.listByUserId(user, Pageable.unpaged());

        assertTrue(result.isPresent());
    }

    @Test
    void countByUserId() {
        User user = userRepository.findById(1L).orElse(null);
        int result = addressRepository.countByUserId(user);

        assertNotEquals(0, result);
    }
}