package com.krasuski.industries.apigateway.handler;

import com.krasuski.industries.apigateway.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class PrintServiceHandler {

    private final AuthService authService;

    @Value("${url.base.print}")
    private String printServiceBaseUrl;

    public PrintServiceHandler(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public RouteLocator publicPrintRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/print/option")
                        .filters(f -> f.setPath("/print/options"))
                        .uri(printServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/print/upload")
                        .filters(f -> f
                                .setPath("/slicer/upload")
                                .filter(authService.optionallyAppendUserIdHeader()))
                        .uri(printServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/print/calculate")
                        .filters(f -> f.setPath("/print/calculate")
                                .filter(authService.optionallyAppendUserIdHeader()))
                        .uri(printServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/print/bucket")
                        .filters(f -> f
                                .setPath("/print/bucket")
                                .filter(authService.optionallyAppendUserIdHeader()))
                        .uri(printServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/print/remove")
                        .filters(f -> f
                                .setPath("/print/remove")
                                .filter(authService.optionallyAppendUserIdHeader()))
                        .uri(printServiceBaseUrl))
                .build();
    }

    @Bean
    public RouteLocator securedPrintRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/print/history")
                        .filters(f -> f
                                .setPath("/print/history")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(printServiceBaseUrl))
                .build();
    }

    @Bean
    public RouteLocator adminPrintRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/admin/fdm/print/material/template")
                        .filters(f -> f
                                .setPath("/admin/fdm/print/material/template")
                        )
                        .uri(printServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/admin/fdm/print/material/template")
                        .filters(f -> f
                                .setPath("/admin/fdm/print/material/template")
                        )
                        .uri(printServiceBaseUrl))
                .build();
    }
}
