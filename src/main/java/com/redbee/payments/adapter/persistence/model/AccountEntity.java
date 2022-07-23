package com.redbee.payments.adapter.persistence.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "account")
@Data
public class AccountEntity {

    @Id
    @GeneratedValue
    Long id;

}
