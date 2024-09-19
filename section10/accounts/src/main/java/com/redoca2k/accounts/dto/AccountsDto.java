package com.redoca2k.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(name = "Accounts", description = "Schema to hold Account details")
@Data // Don't use data annotation in entity class like Accounts.java or Customer.java because equals and hashcode method generated creates issue with spring data jpa
public class AccountsDto {

    @Schema(description = "Account Number of the customer", example = "3454433243")
    @NotEmpty(message =  "Account Number cannot be empty")
    @Pattern(regexp = "[0-9]{10}", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(description = "Account Type of Eazy Bank account", example = "Savings")
    @NotEmpty(message =  "Account Type cannot be empty")
    private String accountType;

    @Schema(description = "Branch Address of Eazy Bank account", example = "123 New Street, New York")
    @NotEmpty(message =  "Branch Address cannot be empty")
    private String branchAddress;
}
