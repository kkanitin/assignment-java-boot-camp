package com.example.skooldio.controller;

import com.example.skooldio.entity.Product;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "ProductController")
@RestController
@RequestMapping(path = "v1/product")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    // TODO: 19/2/2565 test all endpoint

    @PostMapping
    @ApiOperation(value = "create product", response = Product.class)
    public ResponseModel<Product> create(@RequestBody Product entity) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            Product product = service.create(entity);
            responseModel.setData(product);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());

        }
        return responseModel;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "update product by id", response = Product.class)
    public ResponseModel<Product> update(@PathVariable Long id, @RequestBody Product entity) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            Product product = service.update(id, entity);
            responseModel.setMsg("Success");
            responseModel.setData(product);

            if (null == product) {
                responseModel.setErrorMsg(String.format("product id %d not found", id));
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/deduct/{id}")
    @ApiOperation(value = "deduct product by id", response = Product.class)
    public ResponseModel<Product> deduct(@PathVariable Long id, @RequestBody int amount) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            Product product = service.updateQuantity(id, amount,"deduct");
            responseModel.setMsg("Success");
            responseModel.setData(product);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/add/{id}")
    @ApiOperation(value = "add product by id", response = Product.class)
    public ResponseModel<Product> add(@PathVariable Long id, @RequestBody int amount) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            Product product = service.updateQuantity(id, amount,"add");
            responseModel.setMsg("Success");
            responseModel.setData(product);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/deductList")
    @ApiOperation(value = "deduct product list", response = Product.class)
    public ResponseListModel<ProductModel> deductList(@RequestBody UpdateProductQuantityListModel model) {
        ResponseListModel<ProductModel> responseListModel = new ResponseListModel<>();
        try {
            List<ProductModel> products = service.updateQuantityList(model,"deduct");
            responseListModel.setMsg("Success");
            responseListModel.setDatas(products);
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @PatchMapping("/addList")
    @ApiOperation(value = "add product list", response = Product.class)
    public ResponseListModel<ProductModel> addList(@RequestBody UpdateProductQuantityListModel model) {
        ResponseListModel<ProductModel> responseListModel = new ResponseListModel<>();
        try {
            List<ProductModel> products = service.updateQuantityList(model,"add");
            responseListModel.setMsg("Success");
            responseListModel.setDatas(products);
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get product by id", response = Product.class)
    public ResponseModel<Product> getById(@PathVariable Long id) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            Product product = service.getById(id);
            responseModel.setData(product);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

            if (null == product) {
                responseModel.setErrorMsg(String.format("product id %d not found", id));
            }

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete product by id", response = Product.class)
    public ResponseModel<Product> delete(@PathVariable Long id) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            service.deleteById(id);
            responseModel.setData(null);
            responseModel.setMsg(String.format("product id %d deleted.", id));
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping
    @ApiOperation(value = "get product as list", response = Product.class)
    public ResponseListModel<Product> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Product> responseListModel = new ResponseListModel<>();
        try {
            List<Product> products = service.getList(page, size, sort, dir);

            int countAll = service.countAll();
            int count = products.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(products);
            responseListModel.setAll(countAll);
            responseListModel.setCount(count);
            responseListModel.setNext(next);
            responseListModel.setMsg("Success");
            responseListModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }
}
