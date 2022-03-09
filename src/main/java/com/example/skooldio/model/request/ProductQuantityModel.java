package com.example.skooldio.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "ProductQuantity")
public class ProductQuantityModel {

    @ApiParam(name = "productId", example = "0", value = "productId", defaultValue = "cardType")
    @ApiModelProperty(name = "productId", example = "0")
    private long productId;

    @ApiParam(name = "quantity", example = "0", value = "quantity", defaultValue = "cardType")
    @ApiModelProperty(name = "quantity", example = "0")
    private int quantity;
}
