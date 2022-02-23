package com.example.skooldio.controller;

import com.example.skooldio.entity.User;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "UserController")
@RestController
@RequestMapping(path = "v1/user")
@Getter
@Setter
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    @ApiOperation(value = "create user", response = User.class)
    public ResponseModel<User> create(@RequestBody User entity) {
        ResponseModel<User> responseModel = new ResponseModel<>();
        try {
            User user = service.create(entity);
            responseModel.setData(user);

            if (null == user) {
                responseModel.setMsg("Failed");
                responseModel.setErrorMsg(String.format("Username %s is duplicate.", entity.getUsername()));
            } else {
                responseModel.setMsg("Success");
                responseModel.setErrorMsg(null);
            }
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());

        }
        return responseModel;
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "update user by id", response = User.class)
    public ResponseModel<User> updateExceptUsername(@PathVariable Long id, @RequestBody String name) {
        ResponseModel<User> responseModel = new ResponseModel<>();
        try {
            User user = service.updateExceptUsername(id, name);
            responseModel.setMsg("Success");
            responseModel.setData(user);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get user by id", response = User.class)
    public ResponseModel<User> getById(@PathVariable Long id) {
        ResponseModel<User> responseModel = new ResponseModel<>();
        try {
            User user = service.getById(id);
            responseModel.setData(user);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

            if (null == user) {
                responseModel.setErrorMsg(String.format("user id %d not found", id));
            }

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete user by id", response = User.class)
    public ResponseModel<User> delete(@PathVariable Long id) {
        ResponseModel<User> responseModel = new ResponseModel<>();
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
    @ApiOperation(value = "get user as list", response = User.class)
    public ResponseListModel<User> listPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<User> responseListModel = new ResponseListModel<>();
        try {
            List<User> users = service.listPaging(page, size, sort, dir);

            int countAll = service.countAll();
            int count = users.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(users);
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
