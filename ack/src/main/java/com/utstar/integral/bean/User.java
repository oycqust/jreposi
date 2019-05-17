package com.utstar.integral.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * created by UTSC1244 at 2019/5/9 0009
 */
@Data
public class User implements Serializable
{
    private String username;
    private String password;
}
