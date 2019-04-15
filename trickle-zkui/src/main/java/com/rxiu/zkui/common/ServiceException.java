package com.rxiu.zkui.common;

import com.rxiu.zkui.core.exception.BasicException;
import com.rxiu.zkui.core.exception.ExceptionResult;

/**
 * @author rxiu
 * @date 2019/4/15
 */
public class ServiceException extends BasicException {

    public ServiceException(ExceptionResult result) {
        super(result);
    }
}
