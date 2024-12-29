package com.krasuski.industries.backend.factory;

import com.krasuski.industries.backend.domain.RefreshToken;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.domain.value.SessionTokenValue;
import net.datafaker.Faker;

public final class UserTestDataFactory {

    private static final Faker FAKER = new Faker();

    public static User aRegisteredUser() {
        return aRegisteredUserWithHashedPassword(new Password(FAKER.internet().password()));
    }

    public static User aRegisteredUserWithHashedPassword(Password password) {
        return User.createRegisteredUser(
                new Email(FAKER.internet().emailAddress()),
                password
        );
    }

    public static User anAnonymousUser() {
        SessionTokenValue sessionToken = new SessionTokenValue(FAKER.internet().uuid());
        return User.createAnonymousUser(sessionToken);
    }

    public static class UserBuilder {
        private Email email = new Email(FAKER.internet().emailAddress());
        private Password hashedPassword = new Password(FAKER.internet().password());
        private SessionTokenValue sessionToken = new SessionTokenValue(FAKER.internet().uuid());
        private boolean isAccountVerified = true;
        private Long id = FAKER.number().randomNumber();
        private RefreshToken refreshToken = RefreshToken.create(id, 100000);

        public UserBuilder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public UserBuilder withHashedPassword(Password hashedPassword) {
            this.hashedPassword = hashedPassword;
            return this;
        }

        public UserBuilder withSessionToken(SessionTokenValue sessionToken) {
            this.sessionToken = sessionToken;
            return this;
        }

        public UserBuilder withAccountVerified(boolean isAccountVerified) {
            this.isAccountVerified = isAccountVerified;
            return this;
        }

        public UserBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder withRefreshToken(RefreshToken refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public User buildRegisteredUser() {
            User registeredUser = User.createRegisteredUser(email, hashedPassword);
            decorateUser(registeredUser);

            return registeredUser;
        }

        public User buildAnonymousUser() {
            User anonymousUser = User.createAnonymousUser(sessionToken);
            decorateUser(anonymousUser);

            return anonymousUser;
        }

        private void decorateUser(User user) {
            if (isAccountVerified) {
                user.verifyAccount();
            }

            user.setId(id);
            user.getRefreshTokens().add(refreshToken);
        }
    }
}
