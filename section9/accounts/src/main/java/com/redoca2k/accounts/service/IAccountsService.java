package com.redoca2k.accounts.service;

import com.redoca2k.accounts.dto.CustomerDto;

public interface IAccountsService {
    void createAccount(CustomerDto customerDto);
    CustomerDto fetchAccount(String mobileNumber);
    boolean updateAccount(CustomerDto customerDto);
    boolean deleteAccount(String mobileNumber);
}
