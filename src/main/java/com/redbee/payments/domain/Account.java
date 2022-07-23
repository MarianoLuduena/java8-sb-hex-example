package com.redbee.payments.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class Account {

    Long id;
    Money baselineBalance;
    ActivityWindow activityWindow;

    /**
     * Calculates the total balance of the account by adding the activity values to the baseline balance.
     */
    public Money calculateBalance() {
        return this.baselineBalance.plus(this.activityWindow.calculateBalance(this.id));
    }

    /**
     * Tries to withdraw a certain amount of money from this account.
     * If successful, creates a new activity with a negative value.
     *
     * @return true if the withdrawal was successful, false if not.
     */
    public boolean withdraw(Money money, Long targetAccountId) {
        if (!mayWithdraw(money)) {
            return false;
        }

        final Activity withdrawal = Activity.of(
                this.id,
                this.id,
                targetAccountId,
                LocalDateTime.now(),
                money
        );
        this.activityWindow.addActivity(withdrawal);
        return true;
    }

    private boolean mayWithdraw(Money money) {
        return this.calculateBalance().minus(money).isPositiveOrZero();
    }

    /**
     * Tries to deposit a certain amount of money to this account.
     * If sucessful, creates a new activity with a positive value.
     *
     * @return true if the deposit was successful, false if not.
     */
    public boolean deposit(Money money, Long sourceAccountId) {
        final Activity deposit = Activity.of(
                this.id,
                sourceAccountId,
                this.id,
                LocalDateTime.now(),
                money
        );
        this.activityWindow.addActivity(deposit);
        return true;
    }

}
