package com.redbee.payments.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Activity {

    Long id;

    /**
     * The account that owns this activity.
     */
    Long ownerAccountId;

    /**
     * The debited account.
     */
    Long sourceAccountId;

    /**
     * The credited account.
     */
    Long targetAccountId;

    /**
     * The timestamp of the activity.
     */
    LocalDateTime timestamp;

    /**
     * The money that was transferred between the accounts.
     */
    Money amount;

    public static Activity of(
            Long ownerAccountId,
            Long sourceAccountId,
            Long targetAccountId,
            LocalDateTime timestamp,
            Money amount
    ) {
        return Activity.builder()
                .id(null)
                .ownerAccountId(ownerAccountId)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .timestamp(timestamp)
                .amount(amount)
                .build();
    }

}
