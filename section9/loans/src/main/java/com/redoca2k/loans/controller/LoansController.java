package com.redoca2k.loans.controller;

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

import com.redoca2k.loans.constants.LoansConstants;
import com.redoca2k.loans.dto.ErrorResponseDto;
import com.redoca2k.loans.dto.LoansContactInfoDto;
import com.redoca2k.loans.dto.LoansDto;
import com.redoca2k.loans.dto.ResponseDto;
import com.redoca2k.loans.service.ILoansService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Setter;

@Tag(
    name = "CRUD Rest APIs for Loans in EazyBank",
    description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH and DELETE Loans"
)
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Setter
public class LoansController {
    private ILoansService iLoansService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansContactInfoDto loansContactInfoDto;

    public LoansController(ILoansService iLoansService) {
        this.iLoansService = iLoansService;
    }
    
    @Operation(
        summary = "Create Loan REST API",
        description = "REST API to create new loan inside EazyBank"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam @Pattern(regexp="(^(0/91)?[7-9][0-9]{9})",message = "Mobile number must be 10 digits") String mobileNumber) {
        iLoansService.createLoan(mobileNumber);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }


    @Operation(
        summary = "Fetch Loan Details REST API",
        description = "REST API to fetch loan details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam @Pattern(regexp="(^(0/91)?[7-9][0-9]{9})",message = "Mobile number must be 10 digits") String mobileNumber) {
        LoansDto loansDto = iLoansService.fetchLoan(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }


    @Operation(
        summary = "Update Loan Details REST API",
        description = "REST API to update loan details based on a loan number"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
        boolean isUpdated = iLoansService.updateLoan(loansDto);
        if(isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        }else{
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }


    @Operation(
        summary = "Delete Loan Details REST API",
        description = "REST API to delete Loan details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanDetails(@RequestParam @Pattern(regexp="(^(0/91)?[7-9][0-9]{9})$",message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = iLoansService.deleteLoan(mobileNumber);
        if(isDeleted) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
        }
    }


    @Operation(
        summary = "Get Build Version",
        description = "This api is used to get the build version of the application"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Http Status OK"),
        @ApiResponse(responseCode = "500", description = "Http Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("build-info")
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity.status(HttpStatus.OK).body(buildVersion);
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
    public ResponseEntity<LoansContactInfoDto > getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(loansContactInfoDto);
    }
}
