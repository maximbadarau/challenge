package com.db.awmd.challenge.service.processors;

import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.service.FundTransferService;

public class EventProcessor extends Thread {

    private final FundTransferService fundTransferService;

    public EventProcessor(FundTransferService fundTransferService) {
        this.fundTransferService = fundTransferService;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                Transfer fundsTransfer = this.fundTransferService.getFundsTransferQueue().poll();
                this.fundTransferService.processFundTransfer(fundsTransfer);
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
        }
    }
}
