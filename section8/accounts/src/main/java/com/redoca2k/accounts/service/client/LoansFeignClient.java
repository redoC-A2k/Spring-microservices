package com.redoca2k.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.redoca2k.accounts.dto.LoansDto;

@FeignClient(name = "loans")
public interface LoansFeignClient {

    @GetMapping(path = "/api/fetch", produces = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam String mobileNumber);
}
