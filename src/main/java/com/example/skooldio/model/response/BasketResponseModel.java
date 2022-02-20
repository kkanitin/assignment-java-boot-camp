package com.example.skooldio.model.response;

import com.example.skooldio.entity.Product;
import com.example.skooldio.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "BasketResponse")
public class BasketResponseModel {

    @XmlElement(name = "user")
    @ApiParam(name = "user", example = "0", value = "user", required = false)
    @ApiModelProperty(name = "user", example = "0", required = false)
    private User user;

    @XmlElement(name = "products")
    @ApiParam(name = "products", example = "0", value = "products", required = false)
    @ApiModelProperty(name = "products", example = "0", required = false)
    private List<ProductModel> products;

    public BasketResponseModel() {
        products = new ArrayList<>();
    }

    public void addProduct(ProductModel product){
        products.add(product);
    }
}
