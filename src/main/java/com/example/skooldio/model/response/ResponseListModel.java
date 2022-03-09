package com.example.skooldio.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "responseList")
public class ResponseListModel<T> {

    @XmlElement(name = "datas")
    @ApiParam(name = "datas", example = "0", value = "datas")
    @ApiModelProperty(name = "datas", example = "0")
    private List<T> datas;

    @XmlElement(name = "msg")
    @ApiParam(name = "msg", example = "0", value = "msg", defaultValue = "msg")
    @ApiModelProperty(name = "msg", example = "0", dataType = "String", value = "example msg")
    private String msg;

    @XmlElement(name = "errorMsg")
    @ApiParam(name = "errorMsg", example = "0", value = "error msg", defaultValue = "error msg")
    @ApiModelProperty(name = "errorMsg", example = "0", dataType = "String", value = "")
    private String errorMsg;

    @XmlElement(name = "all")
    private int all;
    @XmlElement(name = "count")
    private int count;
    @XmlElement(name = "next")
    private int next;

}
