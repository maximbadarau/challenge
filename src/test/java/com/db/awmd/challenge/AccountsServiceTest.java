package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundsTransfer;
import com.db.awmd.challenge.exception.BadRequestException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;

import java.math.BigDecimal;
import java.util.UUID;

import com.db.awmd.challenge.service.FundTransferService;
import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.PostConstruct;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

    private static UUID CONCURRENT_TEST_ACCOUNT_FROM_UUID = UUID.randomUUID();
    private static UUID CONCURRENT_TEST_ACCOUNT_TO_UUID = UUID.randomUUID();
    private static UUID PARALLEL_TEST_ACCOUNT_FROM_UUID = UUID.randomUUID();
    private static UUID PARALLEL_TEST_ACCOUNT_TO_UUID = UUID.randomUUID();

    @Autowired
    private AccountsService accountsService;
    @Autowired
    private FundTransferService fundTransferService;
    @Rule
    public ConcurrentRule concurrently = new ConcurrentRule();
    @Rule
    public RepeatingRule rule = new RepeatingRule();

    @PostConstruct
    public void init() {
        Account accountFrom = new Account(CONCURRENT_TEST_ACCOUNT_FROM_UUID.toString(), BigDecimal.valueOf(1000));
        Account accountTo = new Account(CONCURRENT_TEST_ACCOUNT_TO_UUID.toString(), BigDecimal.valueOf(1));
        accountsService.createAccount(accountFrom);
        accountsService.createAccount(accountTo);
        accountFrom = new Account(PARALLEL_TEST_ACCOUNT_FROM_UUID.toString(), BigDecimal.valueOf(10));
        accountTo = new Account(PARALLEL_TEST_ACCOUNT_TO_UUID.toString(), BigDecimal.valueOf(1));
        accountsService.createAccount(accountFrom);
        accountsService.createAccount(accountTo);
    }

    @Test
    public void addAccount() throws Exception {
        Account account = new Account("Id-123");
        account.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(account);

        assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
    }

    @Test
    public void addAccount_failsOnDuplicateId() throws Exception {
        String uniqueId = "Id-" + System.currentTimeMillis();
        Account account = new Account(uniqueId);
        this.accountsService.createAccount(account);

        try {
            this.accountsService.createAccount(account);
            fail("Should have failed when adding duplicate account");
        } catch (DuplicateAccountIdException ex) {
            assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
        }

    }

    @Test
    @Concurrent(count = 10)
    @Repeating(repetition = 10)
    public void runsMultipleTimes() {
        Account initialStateAccountFrom = this.accountsService.getAccount(CONCURRENT_TEST_ACCOUNT_FROM_UUID.toString());
        Account initialStateAccountTo = this.accountsService.getAccount(CONCURRENT_TEST_ACCOUNT_TO_UUID.toString());
        FundsTransfer fundsTransfer = new FundsTransfer(CONCURRENT_TEST_ACCOUNT_TO_UUID.toString(), BigDecimal.valueOf(10));
        this.fundTransferService.executeFundsTransfer(CONCURRENT_TEST_ACCOUNT_FROM_UUID.toString(), fundsTransfer);
        Account accountFrom = this.accountsService.getAccount(CONCURRENT_TEST_ACCOUNT_FROM_UUID.toString());
        assertNotNull(initialStateAccountFrom);
        assertNotNull(initialStateAccountTo);
        assertNotNull(accountFrom);
    }

//    @Test(expected = BadRequestException.class)
    @Test
    @Concurrent(count = 2)
    @Repeating(repetition = 1)
    public void runsTwoParallelFlows() {
        FundsTransfer fundsTransfer = new FundsTransfer(PARALLEL_TEST_ACCOUNT_FROM_UUID.toString(), BigDecimal.valueOf(10));
        this.fundTransferService.executeFundsTransfer(PARALLEL_TEST_ACCOUNT_FROM_UUID.toString(), fundsTransfer);
    }

}
