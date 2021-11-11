package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Transfer;

import java.util.Queue;

public interface FundsTransferRepository {

  Queue<Transfer> getFundTransfer();

  void addFundTransfer(Transfer transfer);

}
