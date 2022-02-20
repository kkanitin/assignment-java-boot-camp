package com.example.skooldio.controller;

import com.example.skooldio.entity.Address;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "AddressEndpoint")
@RestController
@RequestMapping(path = "v1/address")
public class AddressController {

    private final AddressService service;

    @Autowired
    public AddressController(AddressService service) {
        this.service = service;
    }

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
    @ApiOperation(value = "delete user by id", response = Address.class)
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

    @GetMapping
    @ApiOperation(value = "get user as list", response = Address.class)
    public ResponseListModel<Address> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<Address> responseListModel = new ResponseListModel<>();
        try {
            List<Address> addresses = service.getList(page, size, sort, dir);

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
