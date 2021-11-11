package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.enums.AccountBalanceOperation;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;

public interface AccountsRepository {

  void createAccount(Account account) throws DuplicateAccountIdException;

  Account getAccount(String accountId);

  Account balanceUpdate(String accountId, BigDecimal amount, AccountBalanceOperation accountBalanceOperation);

  void clearAccounts();
}
