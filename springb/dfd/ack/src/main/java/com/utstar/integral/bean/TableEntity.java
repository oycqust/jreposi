package com.utstar.integral.bean;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
@Data
public class TableEntity<T>
{
    private long total;
    private Iterable<T> rows;
}
