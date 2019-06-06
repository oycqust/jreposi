package com.utstar.integral.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @author UTSC0928
 * @date 2018/6/4
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ResultBean {

    public static final String OK = "0";
    public static final String ERROR = "-1";

    private String code;

    private String msg;

    private String viewseconds;

    private String score;

    private List<DetailBean> details;

    private List<Items> items;

    private Integer total;


}
