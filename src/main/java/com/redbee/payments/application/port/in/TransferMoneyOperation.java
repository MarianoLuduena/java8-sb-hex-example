package com.redbee.payments.application.port.in;

import com.redbee.payments.domain.Money;
import lombok.Builder;
import lombok.Value;

public interface TransferMoneyOperation {

    void execute(Command cmd);

    @Value
    @Builder
    class Command {
        Long sourceAccount;
        Long targetAccount;
        Money amount;
    }

}
