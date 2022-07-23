package com.redbee.payments.adapter.memory;

import com.redbee.payments.application.port.out.AccountLockPort;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountLockPersistenceAdapter implements AccountLockPort {

    private final Map<Long, Boolean> lockedAccounts = new ConcurrentHashMap<>();

    @Override
    public boolean lockAccount(Long accountId) {
        return Optional.ofNullable(lockedAccounts.putIfAbsent(accountId, false)).orElse(true);
    }

    @Override
    public void releaseAccount(Long accountId) {
        lockedAccounts.remove(accountId);
    }

}
