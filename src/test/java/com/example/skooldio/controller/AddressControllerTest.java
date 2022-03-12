package com.example.skooldio.controller;

import com.example.skooldio.config.security.AuthUtil;
import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.response.AddressResponseModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.AddressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
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

import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressControllerTest extends ControllerTest {

    public final String RELATIVE_ENDPOINT = "/v1/address";
    public final String ABSOLUTE_ENDPOINT = "/skooldio/api/v1/address";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private AddressService service;
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
    void create() {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        HttpEntity<Address> request = new HttpEntity<>(address1, headers);
        when(service.create(address1)).thenReturn(address1);

        ResponseModel<Address> result = testRestTemplate.postForObject(RELATIVE_ENDPOINT, request, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
    void getById() throws URISyntaxException {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        HttpEntity<Address> request = new HttpEntity<>(headers);
        when(service.getById((long) 10)).thenReturn(address1);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseModel.class);
        assertEquals("Success", result.getBody().getMsg());
    }

    @Test
    void listByUserId() throws URISyntaxException, JsonProcessingException {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        address1.setUser(new User("test", "tom"));
        Address address2 = new Address();
        address2.setId(11);
        address2.setHouseNo("22/23");
        address2.setUser(new User("test", "tom"));

        List<Address> addresses = new ArrayList<>();
        addresses.add(address1);
        addresses.add(address2);
        when(service.listByUserId((long) 10, 0, 20, "id", "asc")).thenReturn(addresses);
        when(service.countByUserId((long) 10)).thenReturn(addresses.size());
        String queryParam = "?dir=asc&page=0&size=20&sort=id";
        HttpEntity<Address> request = new HttpEntity<>(headers);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + AddressController.LIST_BY_USERID_ENDPOINT + "/10" + queryParam);

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseModel.class);
        assertEquals("Success", result.getBody().getMsg());
    }

    @Test
    void updateExceptUserId() throws URISyntaxException {
        Address address = new Address(10L, new User("testuser", "test"), "12/23",
                "", "", "", "", "", "", "", "", 10800, 1);
        AddressResponseModel model = new AddressResponseModel();
        model.setId(10L);
        model.setHouseNo("12/23");
        String json = new Gson().toJson(address);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        when(service.updateExceptUserId(10L, address)).thenReturn(model);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, request, ResponseModel.class);
        assertEquals("Success", result.getBody().getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void listPaging() throws URISyntaxException {
        //mock
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        Address address2 = new Address();
        address2.setId(11);
        address2.setHouseNo("25/26");
        List<Address> addresses = Arrays.asList(address1, address2);
        HttpEntity<Address> request = new HttpEntity<>(headers);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(addresses);
        when(service.countAll()).thenReturn(addresses.size());
        //Act
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT);
        System.out.println("uri : " + uri.getPath());
        ResponseEntity<ResponseListModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseListModel.class);
        //verify
        assertEquals("Success", result.getBody().getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }
}