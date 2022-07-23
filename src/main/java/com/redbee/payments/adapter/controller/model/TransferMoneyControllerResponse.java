package com.redbee.payments.adapter.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TransferMoneyControllerResponse {

    Long sourceAccount;
    Long destinationAccount;
    Long amount;

}
