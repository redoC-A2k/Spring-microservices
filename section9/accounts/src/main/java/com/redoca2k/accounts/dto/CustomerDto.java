package com.redoca2k.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Don't use data annotation in entity class like Accounts.java or Customer.java because equals and hashcode method generated creates issue with spring data jpa
@NoArgsConstructor
@Schema(name = "Customer", description = "Schema to hold Customer and Account details")
public class CustomerDto {
    
    @Schema(description = "Name of the customer", example = "John Doe")
    @NotEmpty(message =  "Name cannot be empty")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    private String name;

    @Schema(description = "Email of the customer", example = "john@doe.com")
    @NotEmpty(message =  "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "Mobile number of the customer", example = "9876543210")
    @NotEmpty(message =  "Mobile number cannot be empty")
    @Pattern(regexp = "(0/91)?[7-9][0-9]{9}", message = "Mobile number should be valid 10 digit number")
    private String mobileNumber;

    @Schema(description = "Account details of the customer")
    private AccountsDto accountsDto;
}
