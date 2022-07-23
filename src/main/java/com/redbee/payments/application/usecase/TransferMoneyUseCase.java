package com.redbee.payments.application.usecase;

import com.redbee.payments.application.port.in.TransferMoneyOperation;
import com.redbee.payments.application.port.out.AccountLockPort;
import com.redbee.payments.application.port.out.LoadAccountPort;
import com.redbee.payments.application.port.out.UpdateAccountStatePort;
import com.redbee.payments.config.ErrorCode;
import com.redbee.payments.config.exception.BusinessException;
import com.redbee.payments.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Slf4j
public class TransferMoneyUseCase implements TransferMoneyOperation {

    private final AccountLockPort accountLockPort;
    private final LoadAccountPort loadAccountPort;
    private final UpdateAccountStatePort updateAccountStatePort;

    public TransferMoneyUseCase(
            final AccountLockPort accountLockPort,
            final LoadAccountPort loadAccountPort,
            final UpdateAccountStatePort updateAccountStatePort
    ) {
        this.accountLockPort = accountLockPort;
        this.loadAccountPort = loadAccountPort;
        this.updateAccountStatePort = updateAccountStatePort;
    }

    @Override
    public void execute(TransferMoneyOperation.Command cmd) {
        log.info("Executing money transference with data {}", cmd);

        if (Objects.equals(cmd.getSourceAccount(), cmd.getTargetAccount())) {
            throw new BusinessException(ErrorCode.SAME_ACCOUNTS);
        }

        final LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

        final Account sourceAccount = loadAccountPort.loadAccount(cmd.getSourceAccount(), baselineDate);
        final Account targetAccount = loadAccountPort.loadAccount(cmd.getTargetAccount(), baselineDate);
        log.info("Found accounts source {} and target {}", sourceAccount, targetAccount);

        final boolean lockedSourceAccount = accountLockPort.lockAccount(sourceAccount.getId());
        if (!lockedSourceAccount) {
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_AVAILABLE);
        }

        if (!sourceAccount.withdraw(cmd.getAmount(), targetAccount.getId())) {
            accountLockPort.releaseAccount(sourceAccount.getId());
            throw new BusinessException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        final boolean lockedTargetAccount = accountLockPort.lockAccount(targetAccount.getId());
        if (!lockedTargetAccount) {
            accountLockPort.releaseAccount(sourceAccount.getId());
            throw new BusinessException(ErrorCode.ACCOUNT_NOT_AVAILABLE);
        }

        if (!targetAccount.deposit(cmd.getAmount(), sourceAccount.getId())) {
            accountLockPort.releaseAccount(sourceAccount.getId());
            accountLockPort.releaseAccount(targetAccount.getId());
            throw new BusinessException(ErrorCode.DEPOSIT_FAILED);
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLockPort.releaseAccount(sourceAccount.getId());
        accountLockPort.releaseAccount(targetAccount.getId());
    }

}
