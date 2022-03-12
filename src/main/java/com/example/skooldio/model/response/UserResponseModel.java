package com.example.skooldio.model.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@ApiModel(description = "UserResponse")
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseModel {

    private long id;
    private String username;
    private String name;
}
