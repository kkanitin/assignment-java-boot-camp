package com.example.skooldio.controller;

import com.example.skooldio.constant.TransactionStatus;
import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.Transaction;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.request.TransactionModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.model.response.TransactionSummaryResponseModel;
import com.example.skooldio.service.TransactionService;
import com.google.gson.Gson;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest extends ControllerTest {

    public final String RELATIVE_ENDPOINT = "/v1/transaction";
    public final String ABSOLUTE_ENDPOINT = "/skooldio/api/v1/transaction";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private TransactionService service;

    @BeforeEach
    public void setup() {
        testRestTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @Test
    @DisplayName("updateStatusToConfirm ต้องได้ success")
    void updateStatusToConfirm() throws URISyntaxException {
        List<Long> ids = Arrays.asList(10L, 11L);
        String json = new Gson().toJson(ids);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        TransactionModel model1 = new TransactionModel();
        model1.setStatus(TransactionStatus.CONFIRM.name());
        TransactionModel model2 = new TransactionModel();
        model2.setStatus(TransactionStatus.CONFIRM.name());

        when(service.updateStatusList(ids, TransactionStatus.CONFIRM.name())).thenReturn(Arrays.asList(model1, model2));

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + TransactionController.CONFIRM_ENDPOINT);

        ResponseEntity<ResponseListModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseListModel.class);
        assertEquals(2, Objects.requireNonNull(result.getBody()).getDatas().size());
        assertEquals("Success", result.getBody().getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void updateStatusToCancelled() throws URISyntaxException {
        TransactionModel model1 = new TransactionModel();
        model1.setStatus(TransactionStatus.CANCELLED.name());
        when(service.updateStatus(10L, TransactionStatus.CANCELLED.name())).thenReturn(model1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + TransactionController.CANCELLED_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseModel.class);
        assertEquals("Success", Objects.requireNonNull(result.getBody()).getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void updateStatusToShipping() throws URISyntaxException {
        TransactionModel model1 = new TransactionModel();
        model1.setStatus(TransactionStatus.SHIPPING.name());
        when(service.updateStatus(10L, TransactionStatus.SHIPPING.name())).thenReturn(model1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + TransactionController.SHIPPING_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseModel.class);
        assertEquals("Success", Objects.requireNonNull(result.getBody()).getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void updateStatusToSucess() throws URISyntaxException {
        TransactionModel model1 = new TransactionModel();
        model1.setStatus(TransactionStatus.SUCCESS.name());
        when(service.updateStatus(10L, TransactionStatus.SUCCESS.name())).thenReturn(model1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + TransactionController.SUCCESS_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseModel.class);
        assertEquals("Success", Objects.requireNonNull(result.getBody()).getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void updateStatusToFailed() throws URISyntaxException {
        TransactionModel model1 = new TransactionModel();
        model1.setStatus(TransactionStatus.FAILED.name());
        when(service.updateStatus(10L, TransactionStatus.FAILED.name())).thenReturn(model1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        URI uri = new URI(IP + port + ABSOLUTE_ENDPOINT + TransactionController.FAILED_ENDPOINT + "/10");

        ResponseEntity<ResponseModel> result = testRestTemplate.exchange(uri, HttpMethod.PATCH, entity, ResponseModel.class);
        assertEquals("Success", Objects.requireNonNull(result.getBody()).getMsg());
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    @DisplayName("listByGroupNumber groupNumber ใช้เพื่อ groupการcheckoutแต่ละครั้ง(การcheckoutแต่ละครั้งจะได้groupnumberเดียวกัน) เพื่อจะได้summaryได้")
    void listByGroupNumber() {
        TransactionSummaryResponseModel model = new TransactionSummaryResponseModel();
        model.setUser(new User("testuser", "test"));
        model.setSummary(1500.50);

        when(service.listByGroupNumber(10)).thenReturn(model);

        ResponseModel<TransactionSummaryResponseModel> result = testRestTemplate.getForObject(RELATIVE_ENDPOINT + TransactionController.GROUP_NUMBER_ENDPOINT + "/10", ResponseModel.class);

        assertEquals("Success", result.getMsg());
    }

    @Test
    void listPaging() {
        Transaction transaction1 = new Transaction();
        transaction1.setUser(new User("testuser", "test"));
        Transaction transaction2 = new Transaction();
        transaction2.setUser(new User("testuser", "test"));
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(service.listPaging(0, 20, "id", "asc")).thenReturn(transactions);
        when(service.countAll()).thenReturn(transactions.size());

        ResponseListModel<Transaction> result = testRestTemplate.getForObject(RELATIVE_ENDPOINT, ResponseListModel.class);

        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
        assertEquals("Success", result.getMsg());
    }

    @Test
    void listByUserId() {
        Transaction transaction1 = new Transaction();
        transaction1.setUser(new User("testuser", "test"));
        Transaction transaction2 = new Transaction();
        transaction2.setUser(new User("testuser", "test"));
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(service.listByUserId((long) 10, 0, 20, "id", "asc")).thenReturn(transactions);
        when(service.countByUserId((long) 10)).thenReturn(transactions.size());
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        params.put("size", "20");
        params.put("sort", "id");
        params.put("dir", "asc");

        ResponseListModel<Address> result = testRestTemplate.getForObject(RELATIVE_ENDPOINT + TransactionController.USER_ENDPOINT + "/10", ResponseListModel.class, params);
        assertEquals("Success", result.getMsg());
        assertEquals(2, result.getCount());
        assertEquals(2, result.getAll());
    }
}