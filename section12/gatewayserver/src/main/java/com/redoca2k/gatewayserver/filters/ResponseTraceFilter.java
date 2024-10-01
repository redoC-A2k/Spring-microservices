package com.redoca2k.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import reactor.core.publisher.Mono;

// Here we used different way instead of implementing GlobalFilter , here we defined bean
@Configuration
public class ResponseTraceFilter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            // because we first call chain.filter(exchange) and then we called "then" method therefore all the code inside then method will be executed after we get response from microservice
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders(); 
                String correlationId = filterUtility.getCorrelationId(requestHeaders);
                if(exchange.getResponse().getHeaders().containsKey(FilterUtility.CORRELATION_ID)) {
                    logger.debug("Updated the correlation id in the outbound headers: {}", correlationId);
                    exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
                }
            }));
        };
    }
}
