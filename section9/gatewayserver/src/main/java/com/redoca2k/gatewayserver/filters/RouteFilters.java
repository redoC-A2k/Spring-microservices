package com.redoca2k.gatewayserver.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
import java.time.LocalDateTime;


@Component
public class RouteFilters {

    RequestTaceFilter requestTaceFilter;
    ResponseTraceFilter responseTraceFilter;

    RouteFilters(RequestTaceFilter requestTaceFilter, ResponseTraceFilter responseTraceFilter) {
        this.requestTaceFilter = requestTaceFilter;
        this.responseTraceFilter = responseTraceFilter;
    }

    public RouterFunction<ServerResponse> routeWithFilters(String path) {
		String service = path.split("/")[2].toUpperCase();
		return RouterFunctions.route()
				.route(RequestPredicates.path(path + "/**")
						.and(RequestPredicates.method(HttpMethod.GET)
								.or(RequestPredicates.method(HttpMethod.POST))
								.or(RequestPredicates.method(HttpMethod.PUT))
								.or(RequestPredicates.method(HttpMethod.PATCH))),
						HandlerFunctions.http())
				.before(BeforeFilterFunctions.rewritePath(path + "/(?<segment>.*)", "/${segment}"))
				.before(request -> {
					request = requestTaceFilter.apply(request);
					return request;
				})
				.filter(LoadBalancerFilterFunctions.lb(service)) // dynamic service name
				.after(AfterFilterFunctions.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
                .after((request,response) -> {
                    response = responseTraceFilter.apply(request,response);
                    return response;
                })
				.build();
	}
}
