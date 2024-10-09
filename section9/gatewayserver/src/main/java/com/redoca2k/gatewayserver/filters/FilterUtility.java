package com.redoca2k.gatewayserver.filters;

import java.util.List;

import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.servlet.function.ServerRequest;

@Component
public class FilterUtility {
    public static final String CORRELATION_ID = "eazybank-correlation-id";

    public String getCorrelationId(HttpHeaders  requestHeaders) {
        if(requestHeaders.get(CORRELATION_ID) != null) {
           List<String> requestHeaderList = requestHeaders.get(CORRELATION_ID); 
           return requestHeaderList.stream().findFirst().get();
        } else {
            return null;
        }
    }

    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value){
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    // public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
    //     return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    // }

    public ServerRequest setCorrelationId(ServerRequest request, String correlationId) {
        ServerRequest requestWithCorrelationId = ServerRequest.from(request)
            .header(CORRELATION_ID, correlationId)
            .build();
        return requestWithCorrelationId;
    }
}
