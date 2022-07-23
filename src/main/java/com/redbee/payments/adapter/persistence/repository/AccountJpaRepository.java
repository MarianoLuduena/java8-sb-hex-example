package com.redbee.payments.adapter.persistence.repository;

import com.redbee.payments.adapter.persistence.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
}
