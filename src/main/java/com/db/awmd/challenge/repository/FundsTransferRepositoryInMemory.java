package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Transfer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class FundsTransferRepositoryInMemory implements FundsTransferRepository {

    @Getter
    private final Queue<Transfer> fundTransfer = new ConcurrentLinkedQueue<>();


    @Override
    public void addFundTransfer(Transfer transfer) {
        this.fundTransfer.add(transfer);
    }
}
