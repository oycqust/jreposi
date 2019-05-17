package com.utstar.integral.bean;

import lombok.Data;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
@Data
public class CommonResult<T> extends Result
{
    private T data;
}
