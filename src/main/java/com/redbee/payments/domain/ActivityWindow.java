package com.redbee.payments.domain;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@ToString(doNotUseGetters = true)
public class ActivityWindow {

    /**
     * The list of account activities within this window.
     */
    private List<Activity> activities;

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public void addActivity(final Activity activity) {
        this.activities.add(activity);
    }

    /**
     * Calculates the balance by summing up the values of all activities within this window.
     */
    public Money calculateBalance(Long accountId) {
        final Money depositBalance = activities.stream()
                .filter(a -> a.getTargetAccountId().equals(accountId))
                .map(Activity::getAmount)
                .reduce(Money.ZERO, Money::plus);

        final Money withdrawalBalance = activities.stream()
                .filter(a -> a.getSourceAccountId().equals(accountId))
                .map(Activity::getAmount)
                .reduce(Money.ZERO, Money::plus);

        return depositBalance.minus(withdrawalBalance);
    }

}
