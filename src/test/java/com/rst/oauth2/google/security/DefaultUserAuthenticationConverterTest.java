package com.rst.oauth2.google.security;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Map;

public class DefaultUserAuthenticationConverterTest {
    //    private UserAuthenticationConverter converter = new org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter();
    private UserAuthenticationConverter converter = new DefaultUserAuthenticationConverter();

    @Test
    public void shouldExtractAuthenticationWhenAuthoritiesIsCollection() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(UserAuthenticationConverter.AUTHORITIES, newArrayList("a1", "a2"));
        Authentication authentication = converter.extractAuthentication(map);

        assertThat(authentication.getAuthorities(), hasSize(2));
    }

    @Test
    public void shouldExtractAuthenticationWhenAuthoritiesIsString() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put(UserAuthenticationConverter.USERNAME, "test_user");
        map.put(UserAuthenticationConverter.AUTHORITIES, "a1,a2");
        Authentication authentication = converter.extractAuthentication(map);

        assertThat(authentication.getAuthorities(), hasSize(2));
    }
}
