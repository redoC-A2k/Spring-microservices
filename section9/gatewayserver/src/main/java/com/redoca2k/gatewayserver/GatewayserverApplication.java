package com.redoca2k.gatewayserver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
// import org.springframework.cloud.gateway.route.RouteLocator;
// import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import com.redoca2k.gatewayserver.filters.FilterUtility;
import com.redoca2k.gatewayserver.filters.RequestTaceFilter;
import com.redoca2k.gatewayserver.filters.RouteFilters;

@SpringBootApplication
public class GatewayserverApplication {

	@Autowired
	RouteFilters routeFilters;

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> eazyBankouteConfig() {
		RouterFunction<ServerResponse> routerFunction = routeFilters.routeWithFilters("/eazybank/accounts")
				.and(routeFilters.routeWithFilters("/eazybank/loans"))
				.and(routeFilters.routeWithFilters("/eazybank/cards"));
		return routerFunction;
		// return routeLocatorBuilder.routes()
		// .route(p -> p // p indicates the path
		// .path("/eazybank/accounts/**")
		// .filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)",
		// "/${segment}")
		// .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
		// .uri("lb://ACCOUNTS")) // the microservice to which request is to be
		// forwarded . Here lb
		// // indicates
		// // to use client side load balancer, we have to define name as it is
		// // mentioned in eureka server since , accounts is capital in eureka
		// // server we also have defined it in capital;
		// .route(p -> p // p indicates the path
		// .path("/eazybank/loans/**")
		// .filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
		// .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
		// .uri("lb://LOANS"))
		// .route(p -> p // p indicates the path
		// .path("/eazybank/cards/**")
		// .filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
		// .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
		// .uri("lb://CARDS"))
		// .build();
	}
}
