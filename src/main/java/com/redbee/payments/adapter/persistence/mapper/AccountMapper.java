package com.redbee.payments.adapter.persistence.mapper;

import com.redbee.payments.adapter.persistence.model.AccountEntity;
import com.redbee.payments.adapter.persistence.model.ActivityEntity;
import com.redbee.payments.domain.Account;
import com.redbee.payments.domain.Activity;
import com.redbee.payments.domain.ActivityWindow;
import com.redbee.payments.domain.Money;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AccountMapper {

    public Account toDomain(
            final AccountEntity account,
            final List<ActivityEntity> activities,
            final Long withdrawalBalance,
            final Long depositBalance
    ) {
        final Money baselineBalance = Money.of(depositBalance).minus(Money.of(withdrawalBalance));

        return Account.builder()
                .id(account.getId())
                .activityWindow(toDomain(activities))
                .baselineBalance(baselineBalance)
                .build();
    }

    private ActivityWindow toDomain(final List<ActivityEntity> activities) {
        final List<Activity> domainActivities = new ArrayList<>();

        for (final ActivityEntity activity : activities) {
            domainActivities.add(activity.toDomain());
        }

        return new ActivityWindow(domainActivities);
    }

}
