package com.example.skooldio.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "card")
public class CardModel {

    @ApiParam(name = "cardType", example = "VISA", value = "cardType", defaultValue = "cardType")
    @ApiModelProperty(name = "cardType", example = "0", dataType = "String", value = "example msg")
    private String cardType;

    @ApiParam(name = "cardNo", example = "1234", value = "cardNo", defaultValue = "cardNo")
    @ApiModelProperty(name = "cardNo", example = "0", dataType = "String", value = "example msg")
    private String cardNo;

    @ApiParam(name = "expireMonth", example = "0", value = "expireMonth", defaultValue = "expireMonth")
    @ApiModelProperty(name = "expireMonth", example = "0", dataType = "String", value = "example msg")
    private String expireMonth;

    @ApiParam(name = "expireYear", example = "0", value = "expireYear", defaultValue = "expireYear")
    @ApiModelProperty(name = "expireYear", example = "0", dataType = "String", value = "example msg")
    private String expireYear;

    @ApiParam(name = "ccvOrCvv", example = "0", value = "ccvOrCvv", defaultValue = "ccvOrCvv")
    @ApiModelProperty(name = "ccvOrCvv", example = "0", dataType = "String", value = "example msg")
    private String ccvOrCvv;
}
