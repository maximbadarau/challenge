package com.db.awmd.challenge.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountBalanceOperation {
    WITHDRAW(BigDecimal::subtract),
    DEPOSIT(BigDecimal::add);

    private BalanceOperation balanceOperation;

    public interface BalanceOperation {
        BigDecimal performAccountOperation(BigDecimal initialState, BigDecimal amount);
    }
}
