package com.example.skooldio.model.request;

import com.example.skooldio.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Getter
@Setter
@ApiModel(description = "BusketCheckout")
public class BusketCheckoutModel {

    @XmlElement(name = "productsId")
    @ApiParam(name = "productsId", example = "0", value = "productsId")
    @ApiModelProperty(name = "productsId", example = "0")
    private List<Integer> productsId;
}
