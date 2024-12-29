package com.krasuski.industries.backend.dto.user.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdatePrivacySettingsRequest {

    @NotNull
    private Boolean isNewsletterSubscriber;
}
