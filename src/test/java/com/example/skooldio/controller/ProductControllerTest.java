package com.example.skooldio.controller;

import com.example.skooldio.config.security.AuthUtil;
import com.example.skooldio.constant.UpdateQuantityMode;
import com.example.skooldio.entity.Product;
import com.example.skooldio.model.request.ProductQuantityModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.ProductService;
import com.google.gson.Gson;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest extends ControllerTest {

    public final String RELATIVE_ENDPOINT = "/v1/product";
    public final String ABSOLUTE_ENDPOINT = "/skooldio/api/v1/product";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private ProductService service;
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
    @DisplayName("หักลบสินค้า ต้องได้ 200")
    void deduct() throws URISyntaxException {
        Product product = new Product();
        product.setId(10L);
        product.setQuantity(2);

        when(service.updateQuantity(10L, 2, UpdateQuantityMode.DEDUCT.name())).thenReturn(product);

        HttpEntity<Integer> request = new HttpEntity<>(2, headers);
        System.out.println("header : "+request.getHeaders());
        System.out.println("body : "+request.getBody());
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + ProductController.DEDUCT_PRODUCT_ENDPOINT + "/10");
        System.out.println("endpoint : " + IP + port + ABSOLUTE_ENDPOINT + ProductController.DEDUCT_PRODUCT_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, request, ResponseModel.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("หักลบสินค้า เป็นlist ต้องได้ 200")
    void deductList() throws URISyntaxException {
        List<ProductModel> productModelList = new ArrayList<>();
        UpdateProductQuantityListModel model = new UpdateProductQuantityListModel();
        model.addProductQuantityModel(new ProductQuantityModel(10L, 2));
        model.addProductQuantityModel(new ProductQuantityModel(11L, 3));

        productModelList.add(new ProductModel(10L, "สินค้า1", 100.0, 5, "detail"));
        productModelList.add(new ProductModel(11L, "สินค้า2", 300.0, 8, "detail"));

        String json = new Gson().toJson(model);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        when(service.updateQuantityList(model, UpdateQuantityMode.DEDUCT.name())).thenReturn(productModelList);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + ProductController.DEDUCT_PRODUCT_ENDPOINT);

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, request, ResponseModel.class);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void listPaging() throws URISyntaxException {
        Product product1 = new Product();
        product1.setId(10L);
        product1.setName("สินค้า1");
        product1.setQuantity(5);
        Product product2 = new Product();
        product1.setId(11L);
        product1.setName("สินค้า2");
        product1.setQuantity(7);
        List<Product> products = Arrays.asList(product1, product2);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(products);
        when(service.countAll()).thenReturn(products.size());
        String queryParam = "?dir=asc&page=0&size=20&sort=id";
        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + queryParam);
        HttpEntity<String> request = new HttpEntity<>(null, headers);

        ResponseEntity<ResponseListModel> result = testRestTemplate.exchange(uri, HttpMethod.GET, request, ResponseListModel.class);
        assertEquals(200, result.getStatusCodeValue());
    }
}