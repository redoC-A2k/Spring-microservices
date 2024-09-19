package com.redoca2k.cards.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.List;

@ConfigurationProperties(prefix = "cards")
@Getter
@Setter
public class CardsContactInfoDto {
    private String message; 
    private Map<String,String> contactDetails;
    private List<String> onCallSupport;
}