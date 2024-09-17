package com.redoca2k.accounts.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.redoca2k.accounts.dto.CardsDto;

@FeignClient("cards")
public interface CardsFeignClient {

    @GetMapping(path = "/api/fetch", produces = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@RequestParam String mobileNumber); // method name can be anything but method signature and method return type must match along with the access type (i.e. public or private access type)
}
