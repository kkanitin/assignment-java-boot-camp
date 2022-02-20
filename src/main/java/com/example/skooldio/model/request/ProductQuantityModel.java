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

    @ApiParam(name = "productId", example = "0", value = "productId", defaultValue = "cardType", required = false )
    @ApiModelProperty(name = "productId", example = "0", required = false)
    private long productId;

    @ApiParam(name = "quantity", example = "0", value = "quantity", defaultValue = "cardType", required = false )
    @ApiModelProperty(name = "quantity", example = "0", required = false)
    private int quantity;
}
