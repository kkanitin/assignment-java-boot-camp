package com.example.skooldio.service;

import com.example.skooldio.constant.UpdateQuantityMode;
import com.example.skooldio.entity.Product;
import com.example.skooldio.model.request.ProductQuantityModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    ProductService service;

    @BeforeEach
    void init() {
        service = new ProductService();
        service.setRepository(productRepository);
    }

    protected Pageable getPageable(int page, int size, String sortString, String dir) {
        if (page < 0) {
            throw new IllegalArgumentException("page index must not be less than zero!");
        }
        if (size < 1 && size != -1) {
            throw new IllegalArgumentException("size must not be less than one!");
        }

        if (size == -1) {
            return Pageable.unpaged();
        }

        Sort.Direction direction;
        if (dir.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        } else {
            direction = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(new Sort.Order(direction,
                sortString, Sort.NullHandling.NULLS_LAST));
        return PageRequest.of(page, size, sort);
    }

    @Test
    @DisplayName("add")
    void updateQuantityAdd() {
        Product product = new Product(10L, "สินค้า ทดสอบ", 250.0, 20, "detail");

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        Product result = service.updateQuantity(10L, 5, UpdateQuantityMode.ADD.name());
        assertEquals(25, result.getQuantity());
    }

    @Test
    @DisplayName("deduct")
    void updateQuantityDeduct() {
        Product product = new Product(10L, "สินค้า ทดสอบ", 250.0, 20, "detail");

        when(productRepository.findById(10L)).thenReturn(Optional.of(product));
        Product result = service.updateQuantity(10L, 5, UpdateQuantityMode.DEDUCT.name());
        assertEquals(15, result.getQuantity());
    }

    @Test
    @DisplayName("add list")
    void updateQuantityListAdd() {
        Product product1 = new Product(10L, "สินค้า ทดสอบ1", 250.0, 20, "detail");
        Product product2 = new Product(11L, "สินค้า ทดสอบ2", 1000.0, 20, "detail");

        when(productRepository.findById(10L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(11L)).thenReturn(Optional.of(product2));
        List<ProductQuantityModel> productQuantityModelList = Arrays.asList(new ProductQuantityModel(10L, 2), new ProductQuantityModel(11L, 5));
        UpdateProductQuantityListModel updateProductQuantityListModel = new UpdateProductQuantityListModel();
        updateProductQuantityListModel.setProductQuantityModelList(productQuantityModelList);
        List<ProductModel> result = service.updateQuantityList(updateProductQuantityListModel, UpdateQuantityMode.ADD.name());
        assertEquals(2, result.size());
        assertEquals(22, result.get(0).getQuantity());
        assertEquals(25, result.get(1).getQuantity());
    }

    @Test
    @DisplayName("deduct list")
    void updateQuantityListDeduct() {
        Product product1 = new Product(10L, "สินค้า ทดสอบ1", 250.0, 20, "detail");
        Product product2 = new Product(11L, "สินค้า ทดสอบ2", 1000.0, 20, "detail");

        when(productRepository.findById(10L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(11L)).thenReturn(Optional.of(product2));
        List<ProductQuantityModel> productQuantityModelList = Arrays.asList(new ProductQuantityModel(10L, 2), new ProductQuantityModel(11L, 5));
        UpdateProductQuantityListModel updateProductQuantityListModel = new UpdateProductQuantityListModel();
        updateProductQuantityListModel.setProductQuantityModelList(productQuantityModelList);

        List<ProductModel> result = service.updateQuantityList(updateProductQuantityListModel, UpdateQuantityMode.DEDUCT.name());
        assertEquals(2, result.size());
        assertEquals(18, result.get(0).getQuantity());
        assertEquals(15, result.get(1).getQuantity());
    }

    @Test
    void getById() {
        Product product1 = new Product(10L, "สินค้า ทดสอบ1", 250.0, 20, "detail");

        when(productRepository.findById(10L)).thenReturn(Optional.of(product1));

        Product result = service.getById(10L);
        assertEquals(10L, result.getId());
    }

    @Test
    void listPaging() {
        Product product1 = new Product(10L, "สินค้า ทดสอบ1", 250.0, 20, "detail");
        Product product2 = new Product(11L, "สินค้า ทดสอบ2", 1000.0, 20, "detail");
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.count()).thenReturn((long) products.size());

        int count = service.countAll();
        assertEquals(2, count);
    }
}