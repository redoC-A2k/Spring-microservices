package com.redoca2k.accounts.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redoca2k.accounts.constants.AccountsConstants;
import com.redoca2k.accounts.dto.CustomerDto;
import com.redoca2k.accounts.dto.ErrorResponseDto;
import com.redoca2k.accounts.dto.ResponseDto;
import com.redoca2k.accounts.service.IAccountsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Tag(
    name = "CRUD Rest Apis for accounts in eazybank", 
    description = "CRUD Rest APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE accounts")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class AccountsController {
    
    private IAccountsService accountsService; // constructor injection

    @Operation(
        summary = "Create Account Rest api",
        description = "This api is used to create a new customer & account in EazyBank"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Http Status Created"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }


    @Operation(
        summary = "Fetch Account Details Rest api",
        description = "This api is used to fetch customer & account details in EazyBank"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Http Status OK"
    )
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam @Pattern(regexp = "(^[0-9]{10})$",message = "Mobile number must be 10 digit number") String mobileNumber) {
        CustomerDto customerDto = accountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @Operation(
        summary = "Update Account Details Rest api",
        description = "This api is used to update customer & account details in EazyBank"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Http Status OK"
        ),
        @ApiResponse(
            responseCode = "417",
            description = "Http Status Expectation Failed"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Http Status Internal Server Error",
            content = @Content( schema = @Schema(implementation = ErrorResponseDto.class))
        )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto) {
        boolean isUpdated = accountsService.updateAccount(customerDto);
        if(isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
        summary = "Delete Account Details Rest api",
        description = "This api is used to delete customer & account details in EazyBank"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Http Status OK"
        ),        
        @ApiResponse(
            responseCode = "417",
            description = "Http Status Expectation Failed"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Http Status Internal Server Error",
            content = @Content( schema = @Schema(implementation = ErrorResponseDto.class))
        )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccountDetails(@RequestParam @Pattern(regexp = "(^[0-9]{10}$)",message = "Mobile number must be 10 digit number") String mobileNumber) {
        boolean isDeleted = accountsService.deleteAccount(mobileNumber);
        if(isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
        }
    }
}
