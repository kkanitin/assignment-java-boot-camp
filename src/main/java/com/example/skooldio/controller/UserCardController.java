package com.example.skooldio.controller;

import com.example.skooldio.entity.UserCard;
import com.example.skooldio.model.request.CardModel;
import com.example.skooldio.model.response.ResponseListModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.model.request.UserCardRequestModel;
import com.example.skooldio.service.UserCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "UserCardController")
@RestController
@RequestMapping(path = "v1/userCard")
@Getter
@Setter
public class UserCardController {

    @Autowired
    private UserCardService service;

    @PostMapping
    @ApiOperation(value = "create usercard", response = UserCard.class)
    public ResponseModel<UserCard> create(@RequestBody UserCardRequestModel model) {
        ResponseModel<UserCard> responseModel = new ResponseModel<>();
        try {
            UserCard userCard = service.create(model);
            responseModel.setData(userCard);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "update usercard by id", response = UserCard.class)
    public ResponseModel<UserCard> update(@PathVariable Long id, @RequestBody CardModel cardModel) {
        ResponseModel<UserCard> responseModel = new ResponseModel<>();
        try {
            UserCard userCard = service.updateCard(id, cardModel);
            responseModel.setData(userCard);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "get usercard by id", response = UserCard.class)
    public ResponseModel<UserCard> getById(@PathVariable Long id) {
        ResponseModel<UserCard> responseModel = new ResponseModel<>();
        try {
            UserCard userCard = service.getById(id);
            responseModel.setData(userCard);
            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);

            if (null == userCard) {
                responseModel.setErrorMsg(String.format("usercard id %d not found", id));
            }

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping("/listByUserId/{userId}")
    @ApiOperation(value = "get usercard by userid", response = UserCard.class)
    public ResponseListModel<UserCard> listByUserId(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<UserCard> responseListModel = new ResponseListModel<>();
        try {
            List<UserCard> userCards = service.getByUserId((long) userId, page, size, sort, dir);
            responseListModel.setDatas(userCards);
            int countAll = service.countAll();
            int count = userCards.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setAll(countAll);
            responseListModel.setCount(count);
            responseListModel.setNext(next);
            responseListModel.setMsg("Success");
            responseListModel.setErrorMsg(null);

            if (null == userCards || userCards.isEmpty()) {
                responseListModel.setErrorMsg(String.format("userid %d not found", userId));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            responseListModel.setMsg("Failed");
            responseListModel.setErrorMsg(ex.getMessage());
        }
        return responseListModel;
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "delete usercard by id", response = UserCard.class)
    public ResponseModel<UserCard> delete(@PathVariable Long id) {
        ResponseModel<UserCard> responseModel = new ResponseModel<>();
        try {
            service.deleteById(id);
            responseModel.setData(null);
            responseModel.setMsg(String.format("usercard id %d deleted.", id));
            responseModel.setErrorMsg(null);

        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }

    @GetMapping
    @ApiOperation(value = "get usercard as list", response = UserCard.class)
    public ResponseListModel<UserCard> listPaging(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        ResponseListModel<UserCard> responseListModel = new ResponseListModel<>();
        try {
            List<UserCard> userCards = service.listPaging(page, size, sort, dir);

            int countAll = service.countAll();
            int count = userCards.size();
            int next = 0;
            if (count >= size) {
                next = page + size;
            }
            if (next >= countAll) {
                next = 0;
            }

            responseListModel.setDatas(userCards);
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
