package com.example.skooldio.service;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import com.example.skooldio.repository.AddressRepository;
import com.example.skooldio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;
    @Mock
    private UserRepository userRepository;
    AddressService service;

    @BeforeEach
    void init() {
        service = new AddressService();
        service.setRepository(addressRepository);
        service.setUserRepository(userRepository);
    }

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

    @Test
    void create() {
        User user = new User("tom", "test");
        user.setId(5);
        Address address = new Address(user, "12/23", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);

        when(service.create(address)).thenReturn(address);

        Address result = service.create(address);

        assertEquals("12/23", result.getHouseNo());
    }

    @Test
    void listByUserId() {
        User user = new User("tom", "test");
        user.setId(5L);
        Address address = new Address(user, "12/23", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        Address address1 = new Address(user, "22/34", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        addresses.add(address1);

        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(addressRepository.listByUserId(user, getPageable(0, 20, "id", "asc"))).thenReturn(Optional.of(addresses));

        List<Address> result = service.listByUserId(5L, 0, 20, "id", "asc");

        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getUser().getId());
        assertEquals(5, result.get(1).getUser().getId());
    }

    @Test
    void countByUserId() {
        service = new AddressService();
        service.setRepository(addressRepository);
        service.setUserRepository(userRepository);

        User user = new User("tom", "test");
        user.setId(10L);
        Address address = new Address(user, "12/23", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        Address address1 = new Address(user, "22/34", "อาคาร ทดสอบ",
                "ชั้น ทดสอบ", "หมู่บ้าน ทดสอบ", "ซอย ทดสอบ", "ถนน ทดสอบ", "เขต ทดสอบ",
                "แขวง ทดสอบ", "จังหวัด ทดสอบ", 10800, 1);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        addresses.add(address1);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(service.countByUserId(10L)).thenReturn(addresses.size());

        Integer size = service.countByUserId(10L);
        assertEquals(2, size);
    }

}