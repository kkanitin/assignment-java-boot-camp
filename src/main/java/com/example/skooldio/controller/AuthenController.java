package com.example.skooldio.controller;


import com.example.skooldio.model.request.AuthenRequestModel;
import com.example.skooldio.model.response.AuthenResponseModel;
import com.example.skooldio.model.response.ResponseModel;
import com.example.skooldio.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Authentication")
@RestController
@RequestMapping(path = "v1/authen")
@Getter
@Setter
public class AuthenController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "Authentication", response = AuthenResponseModel.class)
    public ResponseModel<AuthenResponseModel> create(@RequestBody AuthenRequestModel model) {
        ResponseModel<AuthenResponseModel> responseModel = new ResponseModel<>();
        try {
            AuthenResponseModel res = userService.login(model);
            responseModel.setData(res);

            responseModel.setMsg("Success");
            responseModel.setErrorMsg(null);
        } catch (Exception ex) {
            responseModel.setMsg("Failed");
            responseModel.setErrorMsg(ex.getMessage());
        }
        return responseModel;
    }
}
