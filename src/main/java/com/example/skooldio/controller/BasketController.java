package com.example.skooldio.controller;

import com.example.skooldio.entity.Basket;
import com.example.skooldio.model.request.BasketExceptUserIdModel;
import com.example.skooldio.model.response.BasketResponseModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.model.request.BasketRequestModel;
import com.example.skooldio.service.BasketService;
import com.example.skooldio.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "BasketController")
@RestController
@RequestMapping(path = "v1/basket")
@Getter
@Setter
public class BasketController {

    @Autowired
    private BasketService service;
    @Autowired
    private ProductService productService;

    @PostMapping
    @ApiOperation(value = "create basket", response = Basket.class)
    public ResponseModel<Basket> create(@RequestBody BasketRequestModel model) {
        ResponseModel<Basket> responseModel = new ResponseModel<>();
        try {
            Basket basket = service.create(model);
            responseModel.setData(basket);

            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());

        }
        return responseModel;
    }

    @PutMapping("/checkout/{userId}")
    @ApiOperation(value = "checkout basket by userid", response = Basket.class)
    public ResponseModel<BasketResponseModel> checkout(@PathVariable Long userId, @RequestBody List<Integer> productsIdList) {
        ResponseModel<BasketResponseModel> responseModel = new ResponseModel<>();
        try {
            BasketResponseModel basketResponseModel = service.checkout(userId, productsIdList);
            responseModel.setMsg("Success");
            responseModel.setData(basketResponseModel);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "update basket by id every field except userId", response = Basket.class)
    public ResponseModel<Basket> updateExceptUsername(@PathVariable Long id, @RequestBody BasketExceptUserIdModel model) {
        ResponseModel<Basket> responseModel = new ResponseModel<>();
        try {
            Basket basket = service.updateExceptUserId(id, model);
            responseModel.setMsg("Success");
            responseModel.setData(basket);

            if (null == basket) {
                responseModel.setErrorMsg(String.format("basket id %d not found", id));
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get basket by id", response = Basket.class)
    public ResponseModel<Basket> getById(@PathVariable Long id) {
        ResponseModel<Basket> responseModel = new ResponseModel<>();
        try {
            Basket basket = service.getById(id);
            responseModel.setData(basket);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

            if (null == basket) {
                responseModel.setErrorMsg(String.format("basket id %d not found", id));
            }

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/listByUserId/{userId}")
    @ApiOperation(value = "get basket list by userid", response = Basket.class)
    public ResponseModel<BasketResponseModel> listByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir) {
        ResponseModel<BasketResponseModel> responseModel = new ResponseModel<>();
        try {
            BasketResponseModel baskets = service.listByUserId(userId, page, size, sort, dir);
            responseModel.setData(baskets);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete basket by id", response = Basket.class)
    public ResponseModel<Basket> delete(@PathVariable Long id) {
        ResponseModel<Basket> responseModel = new ResponseModel<>();
        try {
            service.deleteById(id);
            responseModel.setData(null);
            responseModel.setMsg(String.format("user id %d deleted.", id));
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping
    @ApiOperation(value = "get basket as list", response = Basket.class)
    public ResponseListModel<Basket> listPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Basket> responseListModel = new ResponseListModel<>();
        try {
            List<Basket> baskets = service.listPaging(page, size, sort, dir);

            int countAll = service.countAll();
            int count = baskets.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(baskets);
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
