package com.blog.common.exception;

import com.blog.common.enums.ExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BlogException extends RuntimeException {
    private ExceptionEnum exceptionEnum;
}
