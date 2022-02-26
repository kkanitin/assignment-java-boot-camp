package com.example.skooldio.controller;

import com.example.skooldio.constant.UpdateQuantityMode;
import com.example.skooldio.entity.Product;
import com.example.skooldio.model.request.ProductQuantityModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.ProductService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private ProductService service;

    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    @DisplayName("หักลบสินค้า ต้องได้ success")
    void deduct() throws URISyntaxException {
        Product product = new Product();
        product.setId(10L);
        product.setQuantity(2);

        when(service.updateQuantity(10L, 2, UpdateQuantityMode.DEDUCT.name())).thenReturn(product);

        URI uri = new URI("http://localhost:" + port + "/skooldio/api/v1/product/deduct/10");

        ResponseModel<Product> result = testRestTemplate.patchForObject(uri, 2, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
    @DisplayName("หักลบสินค้า เป็นlist ต้องได้ success")
    void deductList() throws URISyntaxException {
        List<ProductModel> productModelList = new ArrayList<>();
        UpdateProductQuantityListModel model = new UpdateProductQuantityListModel();
        model.addProductQuantityModel(new ProductQuantityModel(10L, 2));
        model.addProductQuantityModel(new ProductQuantityModel(11L, 3));

        productModelList.add(new ProductModel(10L, "สินค้า1", 100.0, 5, "detail"));
        productModelList.add(new ProductModel(11L, "สินค้า2", 300.0, 8, "detail"));

        String json = new Gson().toJson(model);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        when(service.updateQuantityList(model, UpdateQuantityMode.DEDUCT.name())).thenReturn(productModelList);

        URI uri = new URI("http://localhost:" + port + "/skooldio/api/v1/product/deductList");

        ResponseModel<Product> result = testRestTemplate.patchForObject(uri, entity, ResponseModel.class);
        assertEquals("Success", result.getMsg());
    }

    @Test
//    @DisplayName("list paging ต้องได้ success")
    void listPaging() {
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

        ResponseListModel<Product> result = testRestTemplate.getForObject("/v1/product", ResponseListModel.class);
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
        assertEquals("Success", result.getMsg());
    }
}