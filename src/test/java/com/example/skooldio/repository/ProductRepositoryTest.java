package com.example.skooldio.repository;

import com.example.skooldio.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void update() {
        productRepository.update(1L, "สินค้าทดสอบ", "update detail for test", 20, 220.0);
        Product productAfterUpdate = productRepository.getById(1L);
        assertEquals("update detail for test", productAfterUpdate.getDetail());
    }

    @Test
    void updateQuantity() {
        productRepository.updateQuantity(1L, 30);
        Product product = productRepository.getById(1L);
        assertEquals(30, product.getQuantity());
    }

    @Test
    void listByName() {
        Optional<List<Product>> result = productRepository.listByName("สินค้า", Pageable.unpaged());
        assertTrue(result.isPresent());
    }

    @Test
    void countByName() {
        Optional<List<Product>> result = productRepository.listByName("สินค้า", Pageable.unpaged());
        int count = productRepository.countByName("สินค้า");
        assertEquals(result.get().size(), count);
    }
}