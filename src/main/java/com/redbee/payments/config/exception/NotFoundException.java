package com.redbee.payments.config.exception;

import com.redbee.payments.config.ErrorCode;

public final class NotFoundException extends GenericException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
