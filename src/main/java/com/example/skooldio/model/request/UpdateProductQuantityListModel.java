package com.example.skooldio.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "UpdateProductQuantityList")
@AllArgsConstructor
public class UpdateProductQuantityListModel {

    @ApiParam(name = "productQuantityModelList", value = "productQuantityModelList", defaultValue = "cardType")
    @ApiModelProperty(name = "productQuantityModelList")
    List<ProductQuantityModel> productQuantityModelList;

    public UpdateProductQuantityListModel() {
        productQuantityModelList = new ArrayList<>();
    }

    public void addProductQuantityModel(ProductQuantityModel model){
        this.productQuantityModelList.add(model);
    }
}
