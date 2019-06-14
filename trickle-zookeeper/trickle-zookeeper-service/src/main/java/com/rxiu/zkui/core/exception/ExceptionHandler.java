package com.rxiu.zkui.core.exception;

import com.rxiu.zkui.common.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author rxiu
 * @date 2019/4/15
 */
@ControllerAdvice
public class ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(value = BasicException.class)
    @ResponseBody
    public ResponseResult handle(BasicException e) {
        return ResponseResult.fail(e.getCode(), e.getMessage());
    }
}
