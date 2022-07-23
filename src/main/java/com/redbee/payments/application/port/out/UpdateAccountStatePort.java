package com.redbee.payments.application.port.out;

import com.redbee.payments.domain.Account;

public interface UpdateAccountStatePort {

    void updateActivities(Account account);

}
