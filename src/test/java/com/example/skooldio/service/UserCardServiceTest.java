package com.example.skooldio.service;

import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
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
class UserCardServiceTest {

    @Mock
    private UserCardRepository userCardRepository;
    @Mock
    private UserRepository userRepository;
    UserCardService service;

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
        service = new UserCardService();
        service.setRepository(userCardRepository);
        service.setUserRepository(userRepository);
    }

    @Test
    void listByUserId() {
        User user = new User("tom", "test");
        user.setId(10L);
        UserCard userCard1 = new UserCard();
        userCard1.setId(10L);
        userCard1.setUser(user);
        userCard1.setCardNo("123456");
        UserCard userCard2 = new UserCard();
        userCard2.setId(11L);
        userCard2.setUser(user);
        userCard2.setCardNo("654321");
        List<UserCard> userCards = Arrays.asList(userCard1, userCard2);

        when(userRepository.findById(10L)).thenReturn(Optional.of(user));
        when(userCardRepository.listByUserId(user, getPageable(0, 20, "id", "asc"))).thenReturn(Optional.of(userCards));
        List<UserCard> result = service.listByUserId(10L, 0, 20, "id", "asc");

        assertEquals(2, result.size());
        assertEquals(10L, result.get(0).getUser().getId());
        assertEquals(10L, result.get(1).getUser().getId());
    }
}