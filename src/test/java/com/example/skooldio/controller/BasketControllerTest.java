package com.example.skooldio.controller;

import com.example.skooldio.config.security.AuthUtil;
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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BasketControllerTest extends ControllerTest {

    public final String RELATIVE_ENDPOINT = "/v1/basket";
    public final String ABSOLUTE_ENDPOINT = "/skooldio/api/v1/basket";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private BasketService service;
    @Mock
    private ProductRepository productRepository;
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
        Basket basket = new Basket();
        User user = new User("testuser", "test");
        user.setId(10);
        Product product = new Product(10, "สินค้าทดสอบ", 100.0, 10, "รายละเอียด");
        basket.setUser(user);
        basket.setProduct(product);
        basket.setQuantity(2);
        HttpEntity<Basket> request = new HttpEntity<>(basket, headers);
        when(service.create(new BasketRequestModel(10L, 10L, 2))).thenReturn(basket);

        ResponseModel<Basket> result = testRestTemplate.postForObject(RELATIVE_ENDPOINT, request, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
    void checkout() throws URISyntaxException {
        ProductService productService = new ProductService();
        productService.setRepository(productRepository);
        List<Integer> listId = Arrays.asList(1, 2);
        String json = new Gson().toJson(listId);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        when(service.checkout(10L, listId)).thenReturn(new BasketResponseModel());

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + BasketController.CHECKOUT_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PUT, request, ResponseModel.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Success", Objects.requireNonNull(result.getBody()).getMsg());
    }

}