package com.example.skooldio.controller;

import com.example.skooldio.entity.Address;
import com.example.skooldio.model.response.AddressResponseModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "AddressEndpoint")
@RestController
@RequestMapping(path = "v1/address")
@Getter
@Setter
public class AddressController {

    public static final String LIST_BY_USERID_ENDPOINT = "/user";

    @Autowired
    private AddressService service;

    @PostMapping
    @ApiOperation(value = "Address user", response = Address.class)
    public ResponseModel<Address> create(@RequestBody Address entity) {
        ResponseModel<Address> responseModel = new ResponseModel<>();
        try {
            Address address = service.create(entity);
            responseModel.setData(address);

            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get Address by id", response = Address.class)
    public ResponseModel<Address> getById(@PathVariable Long id) {
        ResponseModel<Address> responseModel = new ResponseModel<>();
        try {
            Address address = service.getById(id);
            responseModel.setData(address);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

            if (null == address) {
                responseModel.setErrorMsg(String.format("address id %d not found", id));
            }

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete address by id", response = Address.class)
    public ResponseModel<Address> delete(@PathVariable Long id) {
        ResponseModel<Address> responseModel = new ResponseModel<>();
        try {
            service.deleteById(id);
            responseModel.setData(null);
            responseModel.setMsg(String.format("Address id %d deleted.", id));
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping(LIST_BY_USERID_ENDPOINT + "/{userId}")
    @ApiOperation(value = "get address by user id as list", response = Address.class)
    public ResponseListModel<Address> listByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Address> responseListModel = new ResponseListModel<>();
        try {
            List<Address> addresses = service.listByUserId(userId, page, size, sort, dir);

            int countAll = service.countByUserId(userId);
            int count = addresses.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(addresses);
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

    @PatchMapping("/{id}")
    @ApiOperation(value = "update address except userId by id", response = AddressResponseModel.class)
    public ResponseModel<AddressResponseModel> updateExceptUserId(@PathVariable Long id, @RequestBody Address entity) {
        ResponseModel<AddressResponseModel> responseModel = new ResponseModel<>();
        try {
            AddressResponseModel model = service.updateExceptUserId(id, entity);
            responseModel.setMsg("Success");
            responseModel.setData(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping
    @ApiOperation(value = "get address as list", response = Address.class)
    public ResponseListModel<Address> listPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Address> responseListModel = new ResponseListModel<>();
        try {
            List<Address> addresses = service.listPaging(page, size, sort, dir);

            int countAll = service.countAll();
            int count = addresses.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(addresses);
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
