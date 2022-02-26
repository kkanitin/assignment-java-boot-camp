package com.example.skooldio.controller;

import com.example.skooldio.entity.Basket;
import com.example.skooldio.entity.Product;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.request.BasketRequestModel;
import com.example.skooldio.model.response.BasketResponseModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.repository.ProductRepository;
import com.example.skooldio.service.BasketService;
import com.example.skooldio.service.ProductService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasketControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private BasketService service;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
//    @DisplayName("create basket ต้องได้ผล success")
    void create() {
        Basket basket = new Basket();
        User user = new User("testuser", "test");
        user.setId(10);
        Product product = new Product(10, "สินค้าทดสอบ", 100.0, 10, "รายละเอียด");
        basket.setUser(user);
        basket.setProduct(product);
        basket.setQuantity(2);

        when(service.create(new BasketRequestModel(10L, 10L, 2))).thenReturn(basket);

        ResponseModel<Basket> result = testRestTemplate.postForObject("/v1/basket", basket, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
//    @DisplayName("checkout basket ต้องได้ผล success")
    void checkout() throws URISyntaxException {
        ProductService productService = new ProductService();
        productService.setRepository(productRepository);
        List<Integer> listId = Arrays.asList(1, 2);
        String json = new Gson().toJson(listId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        when(service.checkout(10L, listId)).thenReturn(new BasketResponseModel());

        URI uri = new URI("http://localhost:" + port + "/skooldio/api/v1/basket/checkout/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PUT, entity, ResponseModel.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Success", Objects.requireNonNull(result.getBody()).getMsg());
    }

}