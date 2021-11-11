package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundsTransfer;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.enums.AccountBalanceOperation;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.BadRequestException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.FundsTransferRepository;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Queue;

@Service
public class FundTransferService {

    private final AccountsRepository accountsRepository;
    private final FundsTransferRepository fundsTransferRepository;

    @Autowired
    public FundTransferService(AccountsRepository accountsRepository, FundsTransferRepository fundsTransferRepository) {
        this.accountsRepository = accountsRepository;
        this.fundsTransferRepository = fundsTransferRepository;
    }

    public void executeFundsTransfer(String accountId, FundsTransfer fundsTransfer){
        this.fundsTransferRepository.addFundTransfer(new Transfer(accountId, fundsTransfer.getAccountToId(), fundsTransfer.getAmount()));
    }

    public Queue<Transfer> getFundsTransferQueue() {
        return this.fundsTransferRepository.getFundTransfer();
    }

    public void processFundTransfer(Transfer transfer) {
        validate(transfer.getAccountFrom(), transfer.getAccountTo());
        this.accountsRepository.balanceUpdate(transfer.getAccountFrom(), transfer.getAmount(), AccountBalanceOperation.WITHDRAW);
        this.accountsRepository.balanceUpdate(transfer.getAccountTo(), transfer.getAmount(), AccountBalanceOperation.DEPOSIT);
    }

    private void validate(String accountFromId, String accountToId) {
        if (Strings.isNullOrEmpty(accountFromId)) {
            throw new BadRequestException("Invalid account");
        }
        if (Strings.isNullOrEmpty(accountToId)) {
            throw new BadRequestException("Fund transfer not possible. Beneficiary account not valid.");
        }
        Account accountFrom = this.accountsRepository.getAccount(accountFromId);
        if (accountFrom == null) {
            throw new AccountNotFoundException("Fund transfer not possible. Beneficiary account not found.");
        }
        Account accountTo = this.accountsRepository.getAccount(accountToId);
        if (accountTo == null) {
            throw new BadRequestException("Fund transfer not possible. Beneficiary account not valid.");
        }
    }

}
