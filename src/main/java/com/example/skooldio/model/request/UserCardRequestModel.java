package com.example.skooldio.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "UserCardRequest")
public class UserCardRequestModel {

    private int userId;
    private String cardType;
    private String cardNo;
    private String expireMonth;
    private String expireYear;
    private String ccvOrCvv;
}
