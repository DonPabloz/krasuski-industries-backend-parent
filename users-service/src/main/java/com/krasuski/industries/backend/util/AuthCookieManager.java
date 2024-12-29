package com.krasuski.industries.backend.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthCookieManager {
    public static final String ACCESS_TOKEN_NAME = "access_token";
    public static final String REFRESH_TOKEN_NAME = "refresh_token";

    @Value("${refresh-token-time-to-live}")
    private int refreshTokenTimeToLive;

    @Value("${jwt-time-to-live}")
    private int jwtTimeToLive;

    public Cookie getAccessTokenCookie(HttpServletRequest request) {
        return findCookie(request, ACCESS_TOKEN_NAME);
    }

    public Cookie getRefreshTokenCookie(HttpServletRequest request) {
        return findCookie(request, REFRESH_TOKEN_NAME);
    }

    public void addAccessTokenCookieToResponse(HttpServletResponse response, String cookieValue) {
        addCookieToResponse(response, ACCESS_TOKEN_NAME, cookieValue, jwtTimeToLive);
        log.info("Adding JWT cookie as '" + ACCESS_TOKEN_NAME + "'.");
    }

    public void addRefreshTokenCookieToResponse(HttpServletResponse response, String cookieValue) {
        addCookieToResponse(response, REFRESH_TOKEN_NAME, cookieValue, refreshTokenTimeToLive);
        log.info("Adding refresh token cookie as '" + REFRESH_TOKEN_NAME + "'.");
    }

    public void removeAccessTokenCookieFromResponse(HttpServletResponse response) {
        addCookieToResponse(response, ACCESS_TOKEN_NAME, "", 0);
        log.info("Removing cookie '" + ACCESS_TOKEN_NAME + "'.");
    }

    public void removeRefreshTokenCookieFromResponse(HttpServletResponse response) {
        addCookieToResponse(response, REFRESH_TOKEN_NAME, "", 0);
        log.info("Removing cookie '" + REFRESH_TOKEN_NAME + "'.");
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        log.info("Looking for {} cookie in request.", cookieName);
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(cookieName)) {
                log.info("Found {} cookie in request.", cookieName);
                return cookie;
            }
        }

        log.info("No {} cookie found in request.", cookieName);
        return null;
    }

    private void addCookieToResponse(HttpServletResponse response, String cookieName, String cookieValue, int timeToLive) {
        Cookie cookie = new Cookie(cookieName, cookieValue);

        cookie.setMaxAge(timeToLive);

        //TODO optional properties - figure out what are the responsibilities of those
        cookie.setSecure(false);
        cookie.setHttpOnly(true);

        cookie.setPath("/");

        response.addCookie(cookie);
    }
}
