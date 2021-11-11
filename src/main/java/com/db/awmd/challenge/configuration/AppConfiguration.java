package com.db.awmd.challenge.configuration;

import com.db.awmd.challenge.service.FundTransferService;
import com.db.awmd.challenge.service.processors.EventProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public EventProcessor eventProcessor(@Autowired FundTransferService fundTransferService){
        EventProcessor eventProcessor = new EventProcessor(fundTransferService);
        eventProcessor.start();
        return eventProcessor;
    }
}
