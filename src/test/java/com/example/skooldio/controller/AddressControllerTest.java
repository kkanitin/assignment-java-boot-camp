package com.example.skooldio.controller;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.response.AddressResponseModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.AddressService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.net.*;
import java.util.*;

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

    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    void create() {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        when(service.create(address1)).thenReturn(address1);

        ResponseModel<Address> result = testRestTemplate.postForObject(RELATIVE_ENDPOINT, address1, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
    void getById() {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        when(service.getById((long) 10)).thenReturn(address1);

        ResponseModel<Address> result = testRestTemplate.getForObject(RELATIVE_ENDPOINT + "/10", ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
    void listByUserId() {
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
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "20");
        params.put("sort", "id");
        params.put("dir", "asc");

        String url = RELATIVE_ENDPOINT + AddressController.LIST_BY_USERID_ENDPOINT + "/10";

        ResponseListModel<Address> result = testRestTemplate.getForObject(url, ResponseListModel.class, params);
        assertEquals("Success", result.getMsg());
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
    }

    @Test
    void updateExceptUserId() throws URISyntaxException {
        Address address = new Address(10L, new User("testuser", "test"), "12/23",
                "", "", "", "", "", "", "", "", 10800, 1);
        AddressResponseModel model = new AddressResponseModel();
        model.setId(10L);
        model.setHouseNo("12/23");
        String json = new Gson().toJson(address);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        when(service.updateExceptUserId(10L, address)).thenReturn(model);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseModel.class);
        assertEquals("Success", result.getBody().getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void listPaging() {
        //mock
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        Address address2 = new Address();
        address2.setId(11);
        address2.setHouseNo("25/26");
        List<Address> addresses = Arrays.asList(address1, address2);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(addresses);
        when(service.countAll()).thenReturn(addresses.size());
        //Act
        ResponseListModel<Address> result = testRestTemplate.getForObject(RELATIVE_ENDPOINT, ResponseListModel.class);
        //verify
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
        assertEquals("Success", result.getMsg());
    }
}