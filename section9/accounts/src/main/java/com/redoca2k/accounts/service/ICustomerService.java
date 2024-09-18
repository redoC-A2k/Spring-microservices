package com.redoca2k.accounts.service;

import com.redoca2k.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);
} 