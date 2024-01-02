package io.feedoong.api.service.dto;

import lombok.Getter;

@Getter
public class GoogleUser {
    private String id;
    private String email;
    private Boolean verifiedEmail;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String locale;
}
