package com.rst.oauth2.google.security;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import org.junit.Test;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

public class GoogleAccessTokenConverterTest {
    private DefaultAccessTokenConverter accessTokenConverter = new GoogleAccessTokenConverter();
//    private DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

    @Test
    public void shouldExtractAuthenticationAndScopesWhenScopeIsString() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put(AccessTokenConverter.SCOPE, "a b");
        OAuth2Authentication authentication = accessTokenConverter.extractAuthentication(map);

        assertThat(authentication.getOAuth2Request().getScope(), containsInAnyOrder("a", "b"));
    }

    @Test
    public void shouldExtractAuthenticationAndScopesWhenScopeIsCollection() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put(AccessTokenConverter.SCOPE, newArrayList("a", "b"));
        OAuth2Authentication authentication = accessTokenConverter.extractAuthentication(map);

        assertThat(authentication.getOAuth2Request().getScope(), containsInAnyOrder("a", "b"));
    }
}
