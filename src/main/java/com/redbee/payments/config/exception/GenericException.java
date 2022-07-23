package com.redbee.payments.config.exception;

import com.redbee.payments.config.ErrorCode;

public abstract class GenericException extends RuntimeException {

    private final ErrorCode errorCode;

    protected GenericException(ErrorCode errorCode) {
        super(errorCode.getReasonPhrase());
        this.errorCode = errorCode;
    }

    public ErrorCode getCode() {
        return this.errorCode;
    }

}
