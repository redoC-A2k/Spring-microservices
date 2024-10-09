package com.redoca2k.gatewayserver.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.cglib.core.internal.Function;
import java.util.function.Function;
// import org.springframework.cloud.gateway.filter.GatewayFilterChain;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;

// @Order(1)
// @Component
// public class RequestTaceFilter implements GlobalFilter { // since we have implemented GlobalFilter , our filter will be executed for all type of requests

//     private static final Logger logger = LoggerFactory.getLogger(RequestTaceFilter.class);
//     @Autowired
//     FilterUtility filterUtility;

//     // @Override
//     // public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) { // Wit ServerWebExchange we can get the request and response details of associated excange inside gateway , GatewayFilterChain: all of the filters gets executed in the chain manner, Mono<Void> : it means we are not returning anything from the filter we are just calling the next filter in chain
//     //     // Sometimes in spring reactive we might see Flux instead of Mono, Flux is used when we are returning multiple values while Mono is used when we are returning single value , since we are not returning anything we are using Mono<Void>
//     //     HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
//     //     if(isCorrelationIdPresent(requestHeaders)) {
//     //         logger.debug("eazybank-correlation-id found in the RequestTraceFilter: {}",filterUtility.getCorrelationId(requestHeaders));
//     //     } else {
//     //         String correlationId = generateCorrelationId();
//     //         exchange = filterUtility.setCorrelationId(exchange, correlationId);
//     //         logger.debug("eazybank-correlation-id generated in the RequestTraceFilter: {}",correlationId);
//     //     }
//     //     return chain.filter(exchange); // this will call the next filter in the chain
//     // } 

//     private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
//         if (filterUtility.getCorrelationId(requestHeaders) != null) {
//             return true;
//         }
//         return false;
//     }

//     private String generateCorrelationId() {
//         return java.util.UUID.randomUUID().toString();
//     }
// }
@Component
public class RequestTaceFilter implements Function<ServerRequest, ServerRequest> {
    private static final Logger logger = LoggerFactory.getLogger(RequestTaceFilter.class);

    @Autowired
    private FilterUtility filterUtility;

    public RequestTaceFilter(FilterUtility filterUtility) {
        this.filterUtility = filterUtility;
    }

    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        if (filterUtility.getCorrelationId(requestHeaders) != null) {
            return true;
        }
        return false;
    }

    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

    @Override
    public ServerRequest apply(ServerRequest request) {
        
        HttpHeaders requestHeaders = request.headers().asHttpHeaders();
        if(isCorrelationIdPresent(requestHeaders)) {
            logger.debug("eazybank-correlation-id found in the RequestTraceFilter: {}",filterUtility.getCorrelationId(requestHeaders));
        } else {
            String correlationId = generateCorrelationId();
            // add correlationId to header
            request = filterUtility.setCorrelationId(request, correlationId);
            logger.debug("eazybank-correlation-id generated in the RequestTraceFilter: {}",correlationId);
            
        }
        return request;
    }

    // @Override
    // public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
    //     HttpHeaders requestHeaders = (HttpHeaders) request.headers();
    //     if(isCorrelationIdPresent(requestHeaders)) {
    //         logger.debug("eazybank-correlation-id found in the RequestTraceFilter: {}",filterUtility.getCorrelationId(requestHeaders));
    //     } else {
    //         String correlationId = generateCorrelationId();
    //         // exchange = filterUtility.setCorrelationId(exchange, correlationId);
    //         // add correlationId to header
    //         request.
    //         logger.debug("eazybank-correlation-id generated in the RequestTraceFilter: {}",correlationId);
    //     }
    //     return next.handle(request);
    // }

    
}