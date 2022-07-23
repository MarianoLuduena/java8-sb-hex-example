package com.redbee.payments.adapter.persistence.model;

import com.redbee.payments.domain.Activity;
import com.redbee.payments.domain.Money;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
@Data
public class ActivityEntity {

    public static ActivityEntity from(final Activity domain) {
        final ActivityEntity entity = new ActivityEntity();
        entity.setId(domain.getId());
        entity.setTimestamp(domain.getTimestamp());
        entity.setOwnerAccountId(domain.getOwnerAccountId());
        entity.setSourceAccountId(domain.getSourceAccountId());
        entity.setTargetAccountId(domain.getTargetAccountId());
        entity.setAmount(domain.getAmount().getAmount().longValueExact());
        return entity;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, updatable = false)
    private Long ownerAccountId;

    @Column(nullable = false, updatable = false)
    private Long sourceAccountId;

    @Column(nullable = false, updatable = false)
    private Long targetAccountId;

    @Column(nullable = false, updatable = false)
    private Long amount;

    public Activity toDomain() {
        return Activity.builder()
                .id(id)
                .ownerAccountId(ownerAccountId)
                .sourceAccountId(sourceAccountId)
                .targetAccountId(targetAccountId)
                .timestamp(timestamp)
                .amount(Money.of(amount))
                .build();
    }

}
