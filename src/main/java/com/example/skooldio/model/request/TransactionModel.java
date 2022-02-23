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
@ApiModel(description = "transaction")
public class TransactionModel {

    private long id;
    private Integer userId;
    private Integer userCardId;
    private Integer addressId;
    private Integer productId;
    private Integer quantity;
    private Double amount;
    private String createdDate;
    private String updatedDate;
    private String status;


    public TransactionModel(long id, Integer userId, Integer userCardId, Integer addressId,
                            Integer productId, Integer quantity, Double amount, String status) {
        this.id = id;
        this.userId = userId;
        this.userCardId = userCardId;
        this.addressId = addressId;
        this.productId = productId;
        this.quantity = quantity;
        this.amount = amount;
        this.status = status;
    }
}
