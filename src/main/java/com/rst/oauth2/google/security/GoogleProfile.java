package com.rst.oauth2.google.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GoogleProfile {
    private String id;
    private String email;
    @JsonProperty("verified_email")
    private Boolean verifiedEmail;
    private String name;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    private String link;
    private String picture;
    private String gender;
    private String locale;
    private String hd;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getVerifiedEmail() {
        return verifiedEmail;
    }

    public String getName() {
        return name;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getLink() {
        return link;
    }

    public String getPicture() {
        return picture;
    }

    public String getGender() {
        return gender;
    }

    public String getLocale() {
        return locale;
    }

    public String getHd() {
        return hd;
    }
}
