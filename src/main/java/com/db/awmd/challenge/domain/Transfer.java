package com.db.awmd.challenge.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Transfer {
    public String accountFrom;
    public String accountTo;
    public BigDecimal amount;
}
