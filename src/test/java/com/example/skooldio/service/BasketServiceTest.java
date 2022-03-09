package com.example.skooldio.service;

import com.example.skooldio.constant.CardType;
import com.example.skooldio.entity.*;
import com.example.skooldio.model.response.BasketResponseModel;
import com.example.skooldio.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository repository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressService addressService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserCardService userCardService;
    @Mock
    private UserCardRepository userCardRepository;
    @Mock
    private TransactionService transactionService;
    @Mock
    private ProductService productService;
    BasketService service;

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
        service = new BasketService();
        service.setRepository(repository);
        service.setUserRepository(userRepository);
        service.setProductRepository(productRepository);
        service.setAddressService(addressService);
        service.setUserCardService(userCardService);
        service.setTransactionService(transactionService);
        service.setProductService(productService);
    }

    @Test
    void getByUserid() {
        User user = new User("tom", "test");
        user.setId(10L);
        Address address = new Address(user, "12/23", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        Product product1 = new Product(10L, "สินค้า1", 100.0, 10, "detail");
        Product product2 = new Product(11L, "สินค้า2", 200.0, 10, "detail");
        Basket basket1 = new Basket(10L, user, product1, 1);
        Basket basket2 = new Basket(11L, user, product2, 2);
        List<Basket> baskets = Arrays.asList(basket1, basket2);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(repository.listByUserId(user, getPageable(0, 20, "id", "asc"))).thenReturn(Optional.of(baskets));

        BasketResponseModel result = service.listByUserId(user.getId(), 0, 20, "id", "asc");
        assertEquals(2, result.getProducts().size());
        assertEquals(10L, result.getProducts().get(0).getId());
        assertEquals(10L, result.getUser().getId());
    }

    @Test
    @DisplayName("ต้องได้ emptylist เพราะ checkout product ใน basket หมดแล้ว")
    void checkout() {
        User user = new User("tom", "test");
        user.setId(10L);
        Address address = new Address(user, "12/23", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        Address address1 = new Address(user, "22/34", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        UserCard userCard1 = new UserCard(10L, user, CardType.MASTER_CARD.name(), "1234", "02", "28", "245", 1);
        UserCard userCard2 = new UserCard(11L, user, CardType.AMERICAN_EXPRESS.name(), "4321", "02", "28", "245", 2);
        Product product1 = new Product(10L, "สินค้า1", 100.0, 20, "detail");
        Product product2 = new Product(11L, "สินค้า2", 300.0, 20, "detail");
        Basket basket1 = new Basket(10L, user, product1, 3);
        Basket basket2 = new Basket(11L, user, product2, 5);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(repository.listByUserId(user, getPageable(0, -1, "id", "asc"))).thenReturn(Optional.of(Arrays.asList(basket1, basket2)));

        BasketResponseModel result = service.checkout(10L, Arrays.asList(10, 11));
        assertEquals(0, result.getProducts().size());
    }
}