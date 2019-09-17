package com.blog.common.back;

import com.github.pagehelper.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListTemplate<T> {
    private int page;
    private int pageSize;
    private int count;
    private T list;
}
