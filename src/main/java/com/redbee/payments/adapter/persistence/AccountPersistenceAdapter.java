package com.redbee.payments.adapter.persistence;

import com.redbee.payments.adapter.persistence.mapper.AccountMapper;
import com.redbee.payments.adapter.persistence.model.AccountEntity;
import com.redbee.payments.adapter.persistence.model.ActivityEntity;
import com.redbee.payments.adapter.persistence.repository.AccountJpaRepository;
import com.redbee.payments.adapter.persistence.repository.ActivityJpaRepository;
import com.redbee.payments.application.port.out.LoadAccountPort;
import com.redbee.payments.application.port.out.UpdateAccountStatePort;
import com.redbee.payments.config.ErrorCode;
import com.redbee.payments.config.exception.NotFoundException;
import com.redbee.payments.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final AccountJpaRepository accountRepository;
    private final ActivityJpaRepository activityRepository;
    private final AccountMapper accountMapper;

    public AccountPersistenceAdapter(
            final AccountJpaRepository accountRepository,
            final ActivityJpaRepository activityRepository,
            final AccountMapper accountMapper
    ) {
        this.accountRepository = accountRepository;
        this.activityRepository = activityRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Account loadAccount(Long accountId, LocalDateTime baselineDate) {
        log.info("Loading account {} with baselineDate {}", accountId, baselineDate);
        final AccountEntity account =
                accountRepository.findById(accountId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        final List<ActivityEntity> activities =
                activityRepository.findByOwnerSince(accountId, baselineDate);

        final Long withdrawalBalance =
                orZero(activityRepository.getWithdrawalBalanceUntil(accountId, baselineDate));

        final Long depositBalance =
                orZero(activityRepository.getDepositBalanceUntil(accountId, baselineDate));

        log.info("Loaded account {} with withdrawal balance {} and deposit balance {}",
                account, withdrawalBalance, depositBalance);

        return accountMapper.toDomain(account, activities, withdrawalBalance, depositBalance);
    }

    @Override
    public void updateActivities(Account account) {
        account.getActivityWindow().getActivities().forEach(activity -> {
            if (Objects.isNull(activity.getId())) {
                final ActivityEntity activityToSave = ActivityEntity.from(activity);
                log.info("Saving activity {}", activityToSave);
                activityRepository.save(activityToSave);
            }
        });
    }

    private Long orZero(final Long value) {
        return Objects.isNull(value) ? 0L : value;
    }

}
