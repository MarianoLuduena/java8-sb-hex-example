package com.redbee.payments.application.port.out;

public interface AccountLockPort {

    boolean lockAccount(Long accountId);

    void releaseAccount(Long accountId);

}
