package com.redoca2k.cards.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Map;
import java.util.List;

@ConfigurationProperties(prefix = "cards")
public record CardsContactInfoDto(String message, Map<String,String> contactDetails, List<String> onCallSupport) {
    
}