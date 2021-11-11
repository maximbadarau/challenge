package com.db.awmd.challenge;

import com.db.awmd.challenge.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@Profile(value = "test")
public class TestThreadStep {
    private Integer index;
    private Account initialAccountFrom;
    private Account accountFrom;
    private Account initialAccountTo;
    private Account accountTo;
}
