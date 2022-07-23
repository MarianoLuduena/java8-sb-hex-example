package com.redbee.payments.application.port.out;

import com.redbee.payments.domain.Account;

import java.time.LocalDateTime;

public interface LoadAccountPort {

    Account loadAccount(Long accountId, LocalDateTime baselineDate);

}
