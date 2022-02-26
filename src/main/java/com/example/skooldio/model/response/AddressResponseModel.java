package com.example.skooldio.model.response;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "AddressResponse")
public class AddressResponseModel {

    private Long id;
    private Long user;
    private String houseNo;
    private String buildingName;
    private String floor;
    private String village;
    private String soi;
    private String road;
    private String khet;
    private String kwang;
    private String province;
    private Integer postCode;
    private Integer priority;

    public AddressResponseModel(Long user, String houseNo, String buildingName, String floor, String village, String soi, String road, String khet, String kwang, String province, Integer postCode, Integer priority) {
        this.user = user;
        this.houseNo = houseNo;
        this.buildingName = buildingName;
        this.floor = floor;
        this.village = village;
        this.soi = soi;
        this.road = road;
        this.khet = khet;
        this.kwang = kwang;
        this.province = province;
        this.postCode = postCode;
        this.priority = priority;
    }


}
