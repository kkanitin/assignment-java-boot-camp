package com.example.skooldio.controller;

import com.example.skooldio.config.security.AuthUtil;
import com.example.skooldio.constant.UpdateQuantityMode;
import com.example.skooldio.entity.Product;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "ProductController")
@RestController
@RequestMapping(path = "v1/product")
@Getter
@Setter
public class ProductController {

    public static final String LIST_BY_NAME_ENDPOINT = "/name";
    public static final String ADD_PRODUCT_ENDPOINT = "/add";
    public static final String DEDUCT_PRODUCT_ENDPOINT = "/deduct";

    @Autowired
    private ProductService service;

    @PostMapping
    @ApiOperation(value = "create product", response = Product.class)
    public ResponseModel<Product> create(@RequestHeader String jwtToken, @RequestBody Product entity) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                Product product = service.create(entity);
                responseModel.setData(product);
                responseModel.setMsg("Success");
                responseModel.setErrorMsg(null);
            } else {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());

        }
        return responseModel;
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "update product by id", response = Product.class)
    public ResponseModel<Product> update(@RequestHeader String jwtToken, @PathVariable Long id, @RequestBody Product entity) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                Product product = service.update(id, entity);
                responseModel.setMsg("Success");
                responseModel.setData(product);

                if (null == product) {
                    responseModel.setErrorMsg(String.format("product id %d not found", id));
                }
            } else {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping(DEDUCT_PRODUCT_ENDPOINT + "/{id}")
    @ApiOperation(value = "deduct product by id", response = Product.class)
    public ResponseModel<Product> deduct(@RequestHeader String jwtToken, @PathVariable Long id, @RequestBody int amount) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                Product product = service.updateQuantity(id, amount, UpdateQuantityMode.DEDUCT.name());
                responseModel.setMsg("Success");
                responseModel.setData(product);
            } else {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping(ADD_PRODUCT_ENDPOINT + "/{id}")
    @ApiOperation(value = "add product by id", response = Product.class)
    public ResponseModel<Product> add(@RequestHeader String jwtToken, @PathVariable Long id, @RequestBody int amount) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                Product product = service.updateQuantity(id, amount, UpdateQuantityMode.ADD.name());
                responseModel.setMsg("Success");
                responseModel.setData(product);
            } else {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping(DEDUCT_PRODUCT_ENDPOINT)
    @ApiOperation(value = "deduct product list", response = Product.class)
    public ResponseListModel<ProductModel> deductList(@RequestHeader String jwtToken, @RequestBody UpdateProductQuantityListModel model) {
        ResponseListModel<ProductModel> responseListModel = new ResponseListModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                List<ProductModel> products = service.updateQuantityList(model, UpdateQuantityMode.DEDUCT.name());
                responseListModel.setMsg("Success");
                responseListModel.setDatas(products);
            } else {
                responseListModel.setMsg("Failed");
                responseListModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @PatchMapping(ADD_PRODUCT_ENDPOINT)
    @ApiOperation(value = "add product list", response = Product.class)
    public ResponseListModel<ProductModel> addList(@RequestHeader String jwtToken, @RequestBody UpdateProductQuantityListModel model) {
        ResponseListModel<ProductModel> responseListModel = new ResponseListModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                List<ProductModel> products = service.updateQuantityList(model, UpdateQuantityMode.ADD.name());
                responseListModel.setMsg("Success");
                responseListModel.setDatas(products);
            } else {
                responseListModel.setMsg("Failed");
                responseListModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get product by id", response = Product.class)
    public ResponseModel<Product> getById(@RequestHeader String jwtToken, @PathVariable Long id) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                Product product = service.getById(id);
                responseModel.setData(product);
                responseModel.setMsg("Success");
                responseModel.setErrorMsg(null);

                if (null == product) {
                    responseModel.setErrorMsg(String.format("product id %d not found", id));
                }
            } else {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping(LIST_BY_NAME_ENDPOINT)
    @ApiOperation(value = "list product by name", response = Product.class)
    public ResponseListModel<Product> listByName(
            @RequestHeader String jwtToken,
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Product> responseListModel = new ResponseListModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                List<Product> products = service.listByName(name, page, size, sort, dir);
                int countAll = service.countByName(name);
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
            } else {
                responseListModel.setMsg("Failed");
                responseListModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete product by id", response = Product.class)
    public ResponseModel<Product> delete(@RequestHeader String jwtToken, @PathVariable Long id) {
        ResponseModel<Product> responseModel = new ResponseModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                service.deleteById(id);
                responseModel.setData(null);
                responseModel.setMsg(String.format("product id %d deleted.", id));
                responseModel.setErrorMsg(null);
            } else {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping
    @ApiOperation(value = "get product as list", response = Product.class)
    public ResponseListModel<Product> listPaging(
            @RequestHeader String jwtToken,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Product> responseListModel = new ResponseListModel<>();
        try {
            if (AuthUtil.validateToken(jwtToken)) {
                List<Product> products = service.listPaging(page, size, sort, dir);

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
            } else {
                responseListModel.setMsg("Failed");
                responseListModel.setErrorMsg(AuthUtil.JWT_VERIFY_FAILED);
            }

        } catch (Exception ex) {
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }
}
