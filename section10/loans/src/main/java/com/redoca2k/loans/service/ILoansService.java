package com.redoca2k.loans.service;

import com.redoca2k.loans.dto.LoansDto;

public interface ILoansService {
    void createLoan(String mobileNumber);
    LoansDto fetchLoan(String mobileNumber);
    boolean updateLoan(LoansDto loansDto);
    boolean deleteLoan(String mobileNumber);
}
