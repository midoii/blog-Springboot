package com.blog.common.back;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class ReturnJson<K> {
    private boolean status;
    private int code;
    private String msg;
    private K data;


}
