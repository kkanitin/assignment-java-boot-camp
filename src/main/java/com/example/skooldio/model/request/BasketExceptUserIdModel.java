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
@ApiModel(description = "BasketExceptUserId")
public class BasketExceptUserIdModel {

    @ApiParam(name = "productId", example = "0", value = "1", defaultValue = "1", required = false )
    @ApiModelProperty(name = "productId", example = "0", required = false)
    private Long productId;

    @ApiParam(name = "quantity", example = "0", value = "1", defaultValue = "1", required = false )
    @ApiModelProperty(name = "quantity", example = "0", required = false)
    private int quantity;
}
