package com.krasuski.industries.apigateway.service;

import org.springframework.cloud.gateway.filter.GatewayFilter;

public interface AuthService {
    /**
     * If original request contains credentials, validate them and append userId to request.
     * In case original request doesn't contain credentials, forward original request.
     */
    GatewayFilter optionallyAppendUserIdHeader();

    /**
     * Check if request contains valid credentials, if so then append userId to request.
     * If credentials are missing or are invalid return BAD REQUEST response and don't forward request.
     */
    GatewayFilter authenticateRequestAndAppendUserIdHeader();

    /**
     * Check if request contains valid credentials and user has admin role.
     * If credentials are missing or are invalid return BAD REQUEST response and don't forward request.
     */
    GatewayFilter authenticateAdmin();
}
