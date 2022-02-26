package com.example.skooldio.repository;

import com.example.skooldio.constant.TransactionStatus;
import com.example.skooldio.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCardRepository userCardRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void listByUserId() {
        User user = userRepository.findById(1L).get();
        Optional<List<Address>> optionalAddresses = addressRepository.listByUserId(user, Pageable.unpaged());
        Optional<List<UserCard>> optionalUserCards = userCardRepository.listByUserId(user, Pageable.unpaged());
        Address address = optionalAddresses.isPresent() ? optionalAddresses.get().get(0) : new Address();
        UserCard userCard = optionalUserCards.isPresent() ? optionalUserCards.get().get(0) : new UserCard();
        Optional<Product> optionalProduct1 = productRepository.findById(1L);
        Optional<Product> optionalProduct2 = productRepository.findById(2L);
        Product product1 = optionalProduct1.orElseGet(Product::new);
        Product product2 = optionalProduct2.orElseGet(Product::new);
        Date date = new Date(System.currentTimeMillis());

        Transaction transaction1 = new Transaction(user, userCard, address, product1, 3, 300.0, TransactionStatus.PENDING.name(), date);
        Transaction transaction2 = new Transaction(user, userCard, address, product2, 3, 600.0, TransactionStatus.PENDING.name(), date);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        transactionRepository.saveAll(transactions);

        Optional<List<Transaction>> result = transactionRepository.listByUserId(user, Pageable.unpaged());
        assertTrue(result.isPresent());
    }

    @Test
    void countByUserId() {
        User user = userRepository.findById(1L).get();
        Optional<List<Address>> optionalAddresses = addressRepository.listByUserId(user, Pageable.unpaged());
        Optional<List<UserCard>> optionalUserCards = userCardRepository.listByUserId(user, Pageable.unpaged());
        Address address = optionalAddresses.isPresent() ? optionalAddresses.get().get(0) : new Address();
        UserCard userCard = optionalUserCards.isPresent() ? optionalUserCards.get().get(0) : new UserCard();
        Optional<Product> optionalProduct1 = productRepository.findById(1L);
        Optional<Product> optionalProduct2 = productRepository.findById(2L);
        Product product1 = optionalProduct1.orElseGet(Product::new);
        Product product2 = optionalProduct2.orElseGet(Product::new);
        Date date = new Date(System.currentTimeMillis());

        Transaction transaction1 = new Transaction(user, userCard, address, product1, 3, 300.0, TransactionStatus.PENDING.name(), date);
        Transaction transaction2 = new Transaction(user, userCard, address, product2, 3, 600.0, TransactionStatus.PENDING.name(), date);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        transactionRepository.saveAll(transactions);

        Long result = transactionRepository.countByUserId(user);
        assertTrue(result > 0);
    }

    @Test
    void getMaxGroupNumber() {
        Integer result = transactionRepository.getMaxGroupNumber();
        assertNull(result);
    }

    @Test
    void listByGroupNumber() {
        User user = userRepository.findById(1L).get();
        Optional<List<Address>> optionalAddresses = addressRepository.listByUserId(user, Pageable.unpaged());
        Optional<List<UserCard>> optionalUserCards = userCardRepository.listByUserId(user, Pageable.unpaged());
        Address address = optionalAddresses.isPresent() ? optionalAddresses.get().get(0) : new Address();
        UserCard userCard = optionalUserCards.isPresent() ? optionalUserCards.get().get(0) : new UserCard();
        Optional<Product> optionalProduct1 = productRepository.findById(1L);
        Optional<Product> optionalProduct2 = productRepository.findById(2L);
        Product product1 = optionalProduct1.orElseGet(Product::new);
        Product product2 = optionalProduct2.orElseGet(Product::new);
        Date date = new Date(System.currentTimeMillis());

        Transaction transaction1 = new Transaction(user, userCard, address, product1, 3, 300.0, TransactionStatus.PENDING.name(), date);
        transaction1.setGroupNumber(10);
        Transaction transaction2 = new Transaction(user, userCard, address, product2, 3, 600.0, TransactionStatus.PENDING.name(), date);
        transaction2.setGroupNumber(10);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        transactionRepository.saveAll(transactions);

        Optional<List<Transaction>> result = transactionRepository.listByGroupNumber(10, Pageable.unpaged());

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }
}