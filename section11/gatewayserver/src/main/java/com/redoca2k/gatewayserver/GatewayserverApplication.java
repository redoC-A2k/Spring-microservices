package com.redoca2k.gatewayserver;

import java.time.LocalDateTime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import reactor.core.publisher.Mono;

import java.time.Duration;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p // p indicates the path
						.path("/eazybank/accounts/**")
						.filters(f -> f.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(config -> config.setName("accountCircuitBreaker")
												.setFallbackUri("forward:/contactSupport")))
						.uri("lb://ACCOUNTS")) // the microservice to which request is to be forwarded . Here lb
												// indicates
												// to use client side load balancer, we have to define name as it is
												// mentioned in eureka server since , accounts is capital in eureka
												// server we also have defined it in capital;
				.route(p -> p // p indicates the path
						.path("/eazybank/loans/**")
						.filters(f -> f.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.retry(config -> config.setRetries(3).setMethods(HttpMethod.GET)
									// .setExceptions(null)	same as retry-exceptions in application.yml in accounts.yml
									// similarly other methods are also available like setStatuses i.e retry only in when we get this status
									.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000), 2, true))) // factor means multiplier
								.uri("lb://LOANS"))
				.route(p -> p // p indicates the path
						.path("/eazybank/cards/**")
						.filters(f -> f.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver())))
						.uri("lb://CARDS"))
				.build();
	}

	// Since in case of retry for account build-info we have configured wait duration starting from 500, and miltiplier 2 so the api invocation along with retry consideration will take more than 1 second of time 
	// 1 second is the default time for circuit breaker , so meanwhile accounts will be retrying the circuit breaker will break the connection and return fallback uri
	// so we have to increase the time for circuit breaker to 4 seconds
	// which is done by below bean
	@Bean
	public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer (){
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
																.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
																.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());	
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		// end user can only make 1 request per second
		return new RedisRateLimiter(1, 1, 1);
	}

	@Bean
	KeyResolver userKeyResolver() {
		return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user")).defaultIfEmpty("anonymous");
	}
}
