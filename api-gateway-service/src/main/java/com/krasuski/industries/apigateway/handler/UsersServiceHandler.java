package com.krasuski.industries.apigateway.handler;

import com.krasuski.industries.apigateway.service.AuthService;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class UsersServiceHandler {

    private static final String USER_SERVICE = "http://localhost:8084";

    private final AuthService authService;

    public UsersServiceHandler(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public RouteLocator publicUserRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/user/register")
                        .filters(f -> f.setPath("/user/register"))
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/user/validate")
                        .filters(f -> f.setPath("/user/validate"))
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/auth/basic")
                        .filters(f -> f.setPath("/auth/basic"))
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/auth/jwt")
                        .filters(f -> f.setPath("/auth/jwt"))
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/auth/logout")
                        .filters(f -> f.setPath("/auth/logout"))
                        .uri(USER_SERVICE))
                .build();
    }

    @Bean
    public RouteLocator securedUserRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/user/profile")
                        .filters(f -> f.setPath("/user/profile")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/user/profile")
                        .filters(f -> f.setPath("/user/profile")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/user/password")
                        .filters(f -> f.setPath("/user/password")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/addresses")
                        .filters(f -> f.setPath("/addresses")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/addresses/private")
                        .filters(f -> f.setPath("/addresses/private")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/addresses/private")
                        .filters(f -> f.setPath("/addresses/private")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.DELETE)
                        .and()
                        .path("/addresses/private")
                        .filters(f -> f.setPath("/addresses/private")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))

                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/addresses/company")
                        .filters(f -> f.setPath("/addresses/company")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/addresses/company")
                        .filters(f -> f.setPath("/addresses/company")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.DELETE)
                        .and()
                        .path("/addresses/company")
                        .filters(f -> f.setPath("/addresses/company")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/addresses/locker")
                        .filters(f -> f.setPath("/addresses/locker")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/addresses/locker")
                        .filters(f -> f.setPath("/addresses/locker")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.DELETE)
                        .and()
                        .path("/addresses/locker")
                        .filters(f -> f.setPath("/addresses/locker")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/user/privacy")
                        .filters(f -> f.setPath("/user/privacy")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .route(r -> r
                        .method(HttpMethod.PATCH)
                        .and()
                        .path("/user/privacy")
                        .filters(f -> f.setPath("/user/privacy")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader())
                        )
                        .uri(USER_SERVICE))
                .build();
    }
}
