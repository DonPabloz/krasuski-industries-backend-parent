package com.krasuski.industries.backend.domain;

import com.krasuski.industries.backend.domain.value.*;
import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@Setter
public class User {

    private static final int MAX_ADDRESS_COUNT = 6;
    private static final int MAX_INPOST_LOCKER_COUNT = 3;

    private final UserPubId pubId;
    private final Email email;
    private final UserRole role;
    private final SessionTokenValue anonymousUserSession;
    private final List<CompanyAddress> companyAddresses = new ArrayList<>();
    private final List<PrivateAddress> privateAddresses = new ArrayList<>();
    private final List<InpostExternalPubId> inpostLockers = new ArrayList<>();
    private final List<RefreshToken> refreshTokens = new ArrayList<>();

    private Long id;
    private Password hashedPassword;
    private Boolean isAccountVerified;
    private Boolean removed;

    public User(UserPubId pubId, Email email, UserRole role, Password hashedPassword, SessionTokenValue anonymousUserSession) {
        this.pubId = pubId;
        this.email = email;
        this.role = role;
        this.hashedPassword = hashedPassword;
        this.anonymousUserSession = anonymousUserSession;
    }

    public static User createAnonymousUser(SessionTokenValue anonymousUserSession) {
        return new User(generatePubId(), null, UserRole.ANONYMOUS_USER, null, anonymousUserSession);
    }

    public static User createRegisteredUser(Email email, Password hashedPassword) {
        log.info("Creating new registered user with email: {}", email);
        User user = new User(generatePubId(), email, UserRole.REGISTERED_USER, hashedPassword, null);
        user.isAccountVerified = false;
        return user;
    }

    private static UserPubId generatePubId() {
        return new UserPubId(UUID.randomUUID().toString());
    }

    public void verifyAccount() {
        isAccountVerified = true;
    }

    public boolean isRefreshTokenValid(String refreshToken) {
        if (StringUtils.isBlank(refreshToken)) {
            log.error("Refresh token is empty.");
            return false;
        }

        if (refreshTokens.isEmpty()) {
            log.error("User with pub id '{}' has no refresh tokens.", this.pubId);
            return false;
        }

        return refreshTokens.stream()
                .filter(token -> token.getToken().equals(refreshToken))
                .anyMatch(RefreshToken::isValid);
    }

    public RefreshToken createNewRefreshToken(long timeToLiveInSeconds) {
        log.info("Creating new refresh token for user with pubId '{}'.", this.pubId);
        RefreshToken refreshToken = RefreshToken.create(id, timeToLiveInSeconds);
        refreshTokens.add(refreshToken);

        return refreshToken;
    }

    public void logout(RefreshTokenValue refreshTokenValue) {
        refreshTokens.stream().filter(token -> token.getToken().equals(refreshTokenValue.value()))
                .findFirst()
                .ifPresent(RefreshToken::invalidate);
    }

    public boolean isAccountVerified() {
        return isAccountVerified;
    }

    public void setHashedPassword(Password newHashedPassword) {
        log.info("Setting new hashed password for user with pubId '{}'.", this.pubId);
        this.hashedPassword = newHashedPassword;
    }

    public void addCompanyAddress(CompanyAddress companyAddress) {
        if (hasReachedMaxAddressCount()) {
            log.warn("User with pubId '{}' has reached max address count", this.pubId);
            throw new IllegalArgumentException("User has reached max address count");
        }
        companyAddresses.add(companyAddress);
    }

    public void addPrivateAddress(PrivateAddress privateAddress) {
        if (hasReachedMaxAddressCount()) {
            log.warn("User with pubId '{}' has reached max address count", this.pubId);
            throw new IllegalArgumentException("User has reached max address count");
        }
        privateAddresses.add(privateAddress);
    }

    public void addInpostLocker(InpostExternalPubId inpostExternalPubId) {
        if (inpostLockers.size() >= MAX_INPOST_LOCKER_COUNT) {
            log.warn("User with pubId '{}' has reached max inpost locker count", this.pubId);
            throw new IllegalArgumentException("User has reached max inpost locker count");
        }
        inpostLockers.add(inpostExternalPubId);
        log.info("Adding Inpost locker");
    }

    public void removeCompanyAddress(AddressPubId addressPubId) {
        companyAddresses.removeIf(address -> address.getPubId().equals(addressPubId));
    }

    public void removePrivateAddress(AddressPubId addressPubId) {
        privateAddresses.removeIf(address -> address.getPubId().equals(addressPubId));
    }

    private boolean hasReachedMaxAddressCount() {
        return companyAddresses.size() + privateAddresses.size() >= MAX_ADDRESS_COUNT;
    }

    public void removeInpostLocker(InpostExternalPubId inpostExternalPubId) {
        inpostLockers.removeIf(inpostLocker -> inpostLocker.equals(inpostExternalPubId));
    }

    public boolean isMarkedAsRemoved() {
        return removed;
    }
}
