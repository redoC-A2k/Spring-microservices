package com.redoca2k.accounts.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.redoca2k.accounts.dto.AccountsDto;
import com.redoca2k.accounts.dto.CardsDto;
import com.redoca2k.accounts.dto.CustomerDetailsDto;
import com.redoca2k.accounts.dto.LoansDto;
import com.redoca2k.accounts.entity.Accounts;
import com.redoca2k.accounts.entity.Customer;
import com.redoca2k.accounts.exception.ResourceNotFoundException;
import com.redoca2k.accounts.mapper.AccountsMapper;
import com.redoca2k.accounts.mapper.CustomerMapper;
import com.redoca2k.accounts.repository.AccountsRepository;
import com.redoca2k.accounts.repository.CustomerRepository;
import com.redoca2k.accounts.service.ICustomerService;
import com.redoca2k.accounts.service.client.CardsFeignClient;
import com.redoca2k.accounts.service.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerService{
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    
    // we will get below details by invoking other services
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        
        // invoking cards service
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);
        customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        
        // invoking loans service
        ResponseEntity<LoansDto> loansDtoResponseEntityloa = loansFeignClient.fetchLoanDetails(mobileNumber);
        customerDetailsDto.setLoansDto(loansDtoResponseEntityloa.getBody());
        return customerDetailsDto;
    }
       
}
