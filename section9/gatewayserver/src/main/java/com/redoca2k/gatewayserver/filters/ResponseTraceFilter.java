package com.redoca2k.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import java.util.function.BiFunction;
// import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;


// Here we used different way instead of implementing GlobalFilter , here we defined bean
// @Configuration
// public class ResponseTraceFilter {
//     private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

//     @Autowired
//     FilterUtility filterUtility;

//     @Bean
//     public GlobalFilter postGlobalFilter() {
//         return (exchange, chain) -> {
//             // because we first call chain.filter(exchange) and then we called "then" method therefore all the code inside then method will be executed after we get response from microservice
//             return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                 HttpHeaders requestHeaders = exchange.getRequest().getHeaders(); 
//                 String correlationId = filterUtility.getCorrelationId(requestHeaders);
//                 logger.debug("Updated the correlation id in the outbound headers: {}", correlationId);
//                 exchange.getResponse().getHeaders().add(FilterUtility.CORRELATION_ID, correlationId);
//             }));
//         };
//     }
// }

@Component
public class ResponseTraceFilter implements BiFunction<ServerRequest,ServerResponse,ServerResponse> {
    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    FilterUtility filterUtility;

    @Override
    public ServerResponse apply(ServerRequest request, ServerResponse response) {
        HttpHeaders httpHeaders = request.headers().asHttpHeaders();
        String correlationId = filterUtility.getCorrelationId(httpHeaders);
        logger.debug("Updated the correlation id in the outbound headers: {}", correlationId);
        // ServerResponse responseWithCorrelationId = ServerResponse.from(response)
        //     .header(FilterUtility.CORRELATION_ID, correlationId)
        //     .build();
        response.headers().add(FilterUtility.CORRELATION_ID, correlationId);
        return response;
    }
    
}