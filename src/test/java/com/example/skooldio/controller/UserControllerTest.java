package com.example.skooldio.controller;

import com.example.skooldio.config.security.AuthUtil;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest extends ControllerTest {

    public final String RELATIVE_ENDPOINT = "/v1/user";
    public final String ABSOLUTE_ENDPOINT = "/skooldio/api/v1/user";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private UserService service;
    private String token = "";
    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void setup() {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("GRANT");
        token = Jwts
                .builder()
                .setId("skooldio")
                .setSubject("tom")
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) //10 mins
                .signWith(SignatureAlgorithm.HS512,
                        AuthUtil.SECRET.getBytes()).compact();

        headers.set("jwtToken", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void getById() throws URISyntaxException {
        User user = new User("testuser", "test");

        when(service.getById(10L)).thenReturn(user);
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseModel.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void listPaging() throws URISyntaxException {
        //mock
        User uesr1 = new User("testuser", "test");
        uesr1.setId(10);
        User user2 = new User("testuser2", "test2");
        user2.setId(11);
        List<User> users = Arrays.asList(uesr1, user2);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(users);
        when(service.countAll()).thenReturn(users.size());
        String queryParam = "?dir=asc&page=0&size=20&sort=id";
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + queryParam);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        //Act
        ResponseEntity<ResponseListModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseListModel.class);
        //verify
        assertEquals(200, result.getStatusCodeValue());
    }
}