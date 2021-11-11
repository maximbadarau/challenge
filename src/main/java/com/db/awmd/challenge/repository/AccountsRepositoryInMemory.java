package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.enums.AccountBalanceOperation;
import com.db.awmd.challenge.exception.BadRequestException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    //FIXME: Also not a good idea to have business logic in repo layer. Violating Single-responsibility principle
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public Account balanceUpdate(String accountId, BigDecimal amount, AccountBalanceOperation accountBalanceOperation) {
        return accounts.computeIfPresent(accountId, (s, account) -> syncAccount(account, amount, accountBalanceOperation));
    }

    @Override
    //FIXME: I'm not removing this method, but it's extremely terrible idea to have test
    // utility methods in the application core.
    public void clearAccounts() {
        accounts.clear();
    }

    private Account syncAccount(Account account, BigDecimal amount, AccountBalanceOperation accountBalanceOperation) {
        BigDecimal balance = accountBalanceOperation.getBalanceOperation()
                .performAccountOperation(account.getBalance(), amount);
        if (AccountBalanceOperation.WITHDRAW.equals(accountBalanceOperation) && balance.signum() < 0) {
            throw new BadRequestException("Operation unavailable. Insufficient funds");
        }
        account.setBalance(balance);
        return account;
    }

}
