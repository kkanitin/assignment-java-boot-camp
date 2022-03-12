package com.example.skooldio.controller;

import com.example.skooldio.config.security.AuthUtil;
import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.service.UserCardService;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserCardControllerTest extends ControllerTest {

    public final String RELATIVE_ENDPOINT = "/v1/userCard";
    public final String ABSOLUTE_ENDPOINT = "/skooldio/api/v1/userCard";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private UserCardService service;
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
    void listByUserId() throws URISyntaxException {
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

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + UserCardController.USER_ENDPOINT + "/10");

        ResponseEntity<ResponseListModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseListModel.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void listPaging() throws URISyntaxException {
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

        String queryParam = "?dir=asc&page=0&size=20&sort=id";
        HttpEntity<String> request = new HttpEntity<>(null, headers);
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + queryParam);

        ResponseEntity<ResponseListModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseListModel.class);

        assertEquals(200, result.getStatusCodeValue());
    }
}