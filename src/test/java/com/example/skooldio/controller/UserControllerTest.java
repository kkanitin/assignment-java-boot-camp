package com.example.skooldio.controller;

import com.example.skooldio.entity.User;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private UserService service;

    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getById() {
        User user = new User("testuser", "test");

        when(service.getById(10L)).thenReturn(user);

        ResponseModel result = testRestTemplate.getForObject("/v1/user/10", ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
    void listPaging() {
        //mock
        User uesr1 = new User("testuser", "test");
        uesr1.setId(10);
        User user2 = new User("testuser2", "test2");
        user2.setId(11);
        List<User> users = Arrays.asList(uesr1, user2);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(users);
        when(service.countAll()).thenReturn(users.size());
        //Act
        ResponseListModel<User> result = testRestTemplate.getForObject("/v1/user", ResponseListModel.class);
        //verify
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
        assertEquals("Success", result.getMsg());
    }
}