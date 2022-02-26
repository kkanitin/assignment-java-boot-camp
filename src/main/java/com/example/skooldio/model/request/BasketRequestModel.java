package com.example.skooldio.model.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "BasketRequest")
public class BasketRequestModel {

    private Long userId;
    private Long productId;
    private Integer quantity;
}
