package com.example.skooldio.model.response;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "transactionSummaryResponse")
public class TransactionSummaryResponseModel {

    private User user;
    private List<ProductModel> products;
    private Address address;
    private UserCard userCard;
    private Double summary;
    private Date createdDate;
    private Date updatedDate;

    public TransactionSummaryResponseModel() {
        products = new ArrayList<>();
    }

    public void addProducts(ProductModel model) {
        if (null != products){
            products.add(model);
        }
    }
}
