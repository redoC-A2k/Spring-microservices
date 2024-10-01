package com.redoca2k.cards.controller;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redoca2k.cards.constants.CardsConstants;
import com.redoca2k.cards.dto.CardsContactInfoDto;
import com.redoca2k.cards.dto.CardsDto;
import com.redoca2k.cards.dto.ErrorResponseDto;
import com.redoca2k.cards.dto.ResponseDto;
import com.redoca2k.cards.service.ICardsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

@Tag(
    name = "CRUD REST APIs for Cards in EazyBank",
    description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
// @AllArgsConstructor
@Validated
public class CardsController {
    private ICardsService iCardsService; // constructor injection
    private final static Logger logger = LoggerFactory.getLogger(CardsController.class);

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;

    public CardsController(ICardsService iCardsService) {
        this.iCardsService = iCardsService;
    }

    @Operation(
        summary = "Create Card REST API",
        description = "REST API to create new card inside EazyBank"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createCard(@Valid @RequestParam @Pattern(regexp="(^(0/91)?[7-9][0-9]{9})",message = "Mobile number must be 10 digits") String mobileNumber) {
        iCardsService.createCard(mobileNumber);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
    }
    
    @Operation(
        summary = "Fetch Card Details REST API",
        description = "REST API to fetch card details inside EazyBank"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/fetch")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestHeader("eazybank-correlation-id") String correlationId, @RequestParam @Pattern(regexp="(^[0-9]{10})$",message = "Mobile number must be 10 digit number") String mobileNumber) {
        // logger.debug("CardsController : eazybank-correlation-id found: {}", correlationId);
        logger.debug("fetchCardDetails method start");
        CardsDto cardsDto = iCardsService.fetchCard(mobileNumber);
        logger.debug("fetchCardDetails method end");
        return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    @Operation(
        summary = "Update Card Details REST API",
        description = "REST API to update card details inside EazyBank"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
        boolean isUpdated = iCardsService.updateCard(cardsDto);
        if(isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
        summary = "Delete Card Details REST API",
        description = "REST API to delete Card details based on a mobile number"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "HTTP Status OK"),
        @ApiResponse(responseCode = "417", description = "Expectation Failed"),
        @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteCardDetails(@RequestParam @Pattern(regexp="(^(0/91)?[7-9][0-9]{9})",message = "Mobile number must be 10 digits") String mobileNumber) {
        boolean isDeleted = iCardsService.deleteCard(mobileNumber);
        if(isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(CardsConstants.STATUS_200, CardsConstants.MESSAGE_200));
        }
        else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(CardsConstants.STATUS_417, CardsConstants.MESSAGE_417_DELETE));
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
    public ResponseEntity<CardsContactInfoDto> getContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(cardsContactInfoDto);
    }
}