package com.example.skooldio.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "response")
public class ResponseModel<T> {

    @XmlElement(name = "Data")
    @ApiParam(name = "data", example = "0", value = "data")
    @ApiModelProperty(name = "data", example = "0")
    private T Data;

    @XmlElement(name = "msg")
    @ApiParam(name = "msg", example = "0", value = "msg", defaultValue = "msg")
    @ApiModelProperty(name = "msg", example = "0", dataType = "String", value = "example msg")
    private String msg;

    @XmlElement(name = "errorMsg")
    @ApiParam(name = "errorMsg", example = "0", value = "error msg", defaultValue = "error msg")
    @ApiModelProperty(name = "errorMsg", example = "0", dataType = "String", value = "")
    private String errorMsg;
}
