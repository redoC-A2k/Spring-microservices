package com.redoca2k.accounts.controller;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
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
import com.redoca2k.accounts.dto.AccountsContactInfoDto;
import com.redoca2k.accounts.dto.CustomerDto;
import com.redoca2k.accounts.dto.ErrorResponseDto;
import com.redoca2k.accounts.dto.ResponseDto;
import com.redoca2k.accounts.service.IAccountsService;

import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Tag(
    name = "CRUD Rest Apis for accounts in eazybank", 
    description = "CRUD Rest APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE accounts")
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
// @AllArgsConstructor
@Validated
public class AccountsController {
    
    private IAccountsService accountsService; // constructor injection
    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    public AccountsController(IAccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    @Operation(
        summary = "Create Account Rest api",
        description = "This api is used to create a new customer & account in EazyBank"
    )
    @ApiResponse(responseCode = "201", description = "Http Status Created")
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountsService.createAccount(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }


    @Operation(
        summary = "Fetch Account Details Rest api",
        description = "This api is used to fetch customer & account details in EazyBank"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
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
        @ApiResponse(responseCode = "200", description = "Http Status OK"),
        @ApiResponse(responseCode = "417", description = "Http Status Expectation Failed"),
        @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
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
        @ApiResponse(responseCode = "200", description = "Http Status OK"),
        @ApiResponse(responseCode = "417", description = "Http Status Expectation Failed"),
        @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
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

    @Retry(name = "getBuildVersion", fallbackMethod = "getBuildVersionFallback")
    @Operation(
        summary = "Get Build Version",
        description = "This api is used to get the build version of the application"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Http Status OK"),
        @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("build-info")
    public ResponseEntity<String> getBuildVersion() throws TimeoutException{
        logger.debug("getBuildVersion method invoked");
        // throw new NullPointerException();
        throw new TimeoutException();
        // return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
    }

    public ResponseEntity<String> getBuildVersionFallback(Throwable t) {
        logger.debug("getBuildVersionFallback method invoked");
        return ResponseEntity.status(HttpStatus.OK).body("Build version is not available");
    }

    @Operation(
        summary = "Get java Version",
        description = "This api is used to get the java version of the application"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Http Status OK"),
        @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
        summary = "Get Contact info",
        description = "Contact info details that can be reached out in case of any issues"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Http Status OK"),
        @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("contact-info")
    public ResponseEntity<AccountsContactInfoDto > getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(accountsContactInfoDto);
    }
}
