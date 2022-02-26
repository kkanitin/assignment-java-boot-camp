package com.example.skooldio.service;

import com.example.skooldio.constant.TransactionStatus;
import com.example.skooldio.entity.*;
import com.example.skooldio.model.request.TransactionModel;
import com.example.skooldio.model.response.TransactionSummaryResponseModel;
import com.example.skooldio.repository.AddressRepository;
import com.example.skooldio.repository.TransactionRepository;
import com.example.skooldio.repository.UserCardRepository;
import com.example.skooldio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCardService userCardService;
    @Mock
    private UserCardRepository userCardRepository;
    @Mock
    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ProductService productService;
    TransactionService service;

    protected Pageable getPageable(int page, int size, String sortString, String dir) {
        if (page < 0) {
            throw new IllegalArgumentException("page index must not be less than zero!");
        }
        if (size < 1 && size != -1) {
            throw new IllegalArgumentException("size must not be less than one!");
        }

        if (size == -1) {
            return Pageable.unpaged();
        }

        Sort.Direction direction;
        if (dir.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(new Sort.Order(direction,
                sortString, Sort.NullHandling.NULLS_LAST));
        return PageRequest.of(page, size, sort);
    }

    @BeforeEach
    void init() {
        service = new TransactionService();
        service.setRepository(transactionRepository);
        service.setUserService(userService);
        service.setUserCardService(userCardService);
        service.setAddressService(addressService);
        service.setProductService(productService);
    }

    @Test
    void updateStatusList() {
        User user = new User("tom", "Test");
        UserCard userCard = new UserCard();
        userCard.setId(10L);
        Address address = new Address();
        address.setId(10L);
        Product product1 = new Product();
        product1.setId(10L);
        Product product2 = new Product();
        product2.setId(10L);
        Transaction transaction1 = new Transaction(user,
                userCard, address, product1,
                3, 200.0, TransactionStatus.PENDING.name());
        transaction1.setId(10L);
        Transaction transaction2 = new Transaction(user,
                userCard, address, product2,
                4, 500.0, TransactionStatus.PENDING.name());
        transaction2.setId(11L);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.findById(10L)).thenReturn(Optional.of(transaction1));
        when(transactionRepository.findById(11L)).thenReturn(Optional.of(transaction2));

        List<TransactionModel> result = service.updateStatusList(Arrays.asList(10L, 11L), TransactionStatus.CONFIRM.name());

        assertEquals(2, result.size());
        assertEquals(TransactionStatus.CONFIRM.name(), result.get(0).getStatus());
        assertEquals(TransactionStatus.CONFIRM.name(), result.get(1).getStatus());
    }

    @Test
    void listByGroupNumber() {
        User user = new User("tom", "Test");
        UserCard userCard = new UserCard();
        userCard.setId(10L);
        Address address = new Address();
        address.setId(10L);
        Product product1 = new Product();
        product1.setId(10L);
        product1.setPriceBaht(100.0);
        Product product2 = new Product();
        product2.setId(10L);
        product2.setPriceBaht(500.0);
        Transaction transaction1 = new Transaction(user,
                userCard, address, product1,
                3, 300.0, TransactionStatus.PENDING.name());
        transaction1.setId(10L);
        Transaction transaction2 = new Transaction(user,
                userCard, address, product2,
                4, 2000.0, TransactionStatus.PENDING.name());
        transaction2.setId(11L);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionRepository.listByGroupNumber(1, Pageable.unpaged())).thenReturn(Optional.of(transactions));

        TransactionSummaryResponseModel result = service.listByGroupNumber(1);

        assertEquals(2, result.getProducts().size());
        assertEquals(2300.0, result.getSummary());
    }
}