package com.rst.oauth2.google.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Map;

/**
 * Interface to grant authorities based on the values in the map. Open for extension as custom logic for authorities
 * can be added as implementations of the interface and wired in.
 */
public interface AuthorityGranter {
    List<? extends GrantedAuthority> getAuthorities(Map<String, ?> map);
}
