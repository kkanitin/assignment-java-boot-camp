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

    @ApiParam(name = "cardType", example = "VISA", value = "cardType", defaultValue = "cardType", required = false )
    @ApiModelProperty(name = "cardType", example = "0", dataType = "String", value = "example msg", required = false)
    private String cardType;

    @ApiParam(name = "cardNo", example = "1234", value = "cardNo", defaultValue = "cardNo", required = false )
    @ApiModelProperty(name = "cardNo", example = "0", dataType = "String", value = "example msg", required = false)
    private String cardNo;

    @ApiParam(name = "expireMonth", example = "0", value = "expireMonth", defaultValue = "expireMonth", required = false )
    @ApiModelProperty(name = "expireMonth", example = "0", dataType = "String", value = "example msg", required = false)
    private String expireMonth;

    @ApiParam(name = "expireYear", example = "0", value = "expireYear", defaultValue = "expireYear", required = false )
    @ApiModelProperty(name = "expireYear", example = "0", dataType = "String", value = "example msg", required = false)
    private String expireYear;

    @ApiParam(name = "ccvOrCvv", example = "0", value = "ccvOrCvv", defaultValue = "ccvOrCvv", required = false )
    @ApiModelProperty(name = "ccvOrCvv", example = "0", dataType = "String", value = "example msg", required = false)
    private String ccvOrCvv;
}
