package com.krasuski.industries.backend.exception;

import java.util.UUID;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(UUID publicId) {
        super("Address with publicId: " + publicId + " was not found.");
    }
}
