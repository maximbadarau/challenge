package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class FundsTransfer {

    @NotNull
    @NotEmpty
    private final String accountToId;

    @NotNull
    @Min(value = 0, message = "The amount to transfer should always be a positive number.")
    private BigDecimal amount;

    @JsonCreator
    public FundsTransfer(@JsonProperty("accountToId") String accountToId,
                         @JsonProperty("amount") BigDecimal amount) {
        this.accountToId = accountToId;
        this.amount = amount;
    }

}
