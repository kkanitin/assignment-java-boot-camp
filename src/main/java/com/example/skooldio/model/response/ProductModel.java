package com.example.skooldio.model.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Product")
public class ProductModel {

    private long id;
    private String name;
    private Double priceBaht;
    private int quantity;
    private String detail;
}
