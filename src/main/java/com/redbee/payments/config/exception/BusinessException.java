package com.redbee.payments.config.exception;

import com.redbee.payments.config.ErrorCode;

public final class BusinessException extends GenericException {

    public BusinessException(ErrorCode errorCode) {
        super(errorCode);
    }

}
