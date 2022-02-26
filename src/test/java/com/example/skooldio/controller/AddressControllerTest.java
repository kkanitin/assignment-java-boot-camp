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
class AddressControllerTest {

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
//    @DisplayName("create address ต้องได้ผล success")
    void create() {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        when(service.create(address1)).thenReturn(address1);

        ResponseModel<Address> result = testRestTemplate.postForObject("/v1/address", address1, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
//    @DisplayName("get by id ต้องได้ผล success")
    void getById() {
        Address address1 = new Address();
        address1.setId(10);
        address1.setHouseNo("12/23");
        when(service.getById((long) 10)).thenReturn(address1);

        ResponseModel<Address> result = testRestTemplate.getForObject("/v1/address/10", ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
//    @DisplayName("ลบ ต้องลบได้ ได้ผล 200 ok")
    void deleteSuccess() throws IOException {
        URL url = new URL("http://localhost:" + port + "/skooldio/api/v1/address/4");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        assertEquals(200, http.getResponseCode());
        http.disconnect();

    }

    @Test
//    @DisplayName("get by userid (10) ต้องได้ success")
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

        ResponseListModel<Address> result = testRestTemplate.getForObject("/v1/address/listByUserId/10", ResponseListModel.class, params);
        assertEquals("Success", result.getMsg());
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
    }

    @Test
//    @DisplayName("update ยกเว้น userId ต้องได้success")
    void updateExceptUserId() throws URISyntaxException {
        Address address = new Address(10L, new User("testuser", "test"), "12/23",
                "", "", "", "", "", "", "", "", 10800, 1);
        AddressResponseModel model = new AddressResponseModel();
        model.setId(10L);
        model.setHouseNo("12/23");
        String json = new Gson().toJson(address);
        System.out.println("json : " + json);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        when(service.updateExceptUserId(10L, address)).thenReturn(model);

        URI uri = new URI("http://localhost:" + port + "/skooldio/api/v1/address/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseModel.class);
        assertEquals("Success", result.getBody().getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
//    @DisplayName("list paging ต้องได้ค่า msg เป็น success")
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
        ResponseListModel<Address> result = testRestTemplate.getForObject("/v1/address", ResponseListModel.class);
        //verify
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
        assertEquals("Success", result.getMsg());
    }
}