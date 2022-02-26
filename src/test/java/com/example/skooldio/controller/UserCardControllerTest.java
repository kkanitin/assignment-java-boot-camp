package com.example.skooldio.controller;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.service.UserCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserCardControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private UserCardService service;

    @Test
    void listByUserId() {
        UserCard userCard1 = new UserCard();
        userCard1.setId(10L);
        userCard1.setCardNo("152468486948");
        userCard1.setUser(new User("test", "tom"));
        UserCard userCard2 = new UserCard();
        userCard2.setId(11L);
        userCard2.setCardNo("8648696588");
        userCard2.setUser(new User("test", "tom"));

        List<UserCard> userCards = new ArrayList<>();
        userCards.add(userCard1);
        userCards.add(userCard2);
        when(service.listByUserId((long) 10, 0, 20, "id", "asc")).thenReturn(userCards);
        when(service.countByUserId((long) 10)).thenReturn(userCards.size());
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "20");
        params.put("sort", "id");
        params.put("dir", "asc");

        ResponseListModel<Address> result = testRestTemplate.getForObject("/v1/userCard/listByUserId/10", ResponseListModel.class, params);
        assertEquals("Success", result.getMsg());
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
    }

    @Test
    void listPaging() {
        UserCard userCard1 = new UserCard();
        userCard1.setId(10L);
        userCard1.setCardNo("152468486948");
        userCard1.setUser(new User("test", "tom"));
        UserCard userCard2 = new UserCard();
        userCard2.setId(11L);
        userCard2.setCardNo("8648696588");
        userCard2.setUser(new User("test", "tom"));

        List<UserCard> userCards = Arrays.asList(userCard1, userCard2);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(userCards);
        when(service.countAll()).thenReturn(userCards.size());

        ResponseListModel<Address> result = testRestTemplate.getForObject("/v1/userCard", ResponseListModel.class);

        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
        assertEquals("Success", result.getMsg());
    }
}