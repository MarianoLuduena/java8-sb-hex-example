package com.redbee.payments.adapter.controller.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.redbee.payments.application.port.in.TransferMoneyOperation;
import com.redbee.payments.domain.Money;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransferMoneyControllerRequest {

    @NotNull @Positive Long targetAccount;
    @NotNull @Positive Long amount;

    public TransferMoneyOperation.Command toDomain(final Long sourceAccount) {
        return TransferMoneyOperation.Command.builder()
                .sourceAccount(sourceAccount)
                .targetAccount(targetAccount)
                .amount(Money.of(amount))
                .build();
    }

}
