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
    @ApiParam(name = "productsId", example = "0", value = "productsId", required = false)
    @ApiModelProperty(name = "productsId", example = "0", required = false)
    private List<Integer> productsId;
}
