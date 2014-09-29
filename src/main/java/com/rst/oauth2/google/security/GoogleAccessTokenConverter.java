/*******************************************************************************
 *     Cloud Foundry
 *     Copyright (c) [2009-2014] Pivotal Software, Inc. All Rights Reserved.
 *
 *     This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *     You may not use this product except in compliance with the License.
 *
 *     This product includes a number of subcomponents with
 *     separate copyright notices and license terms. Your use of these
 *     subcomponents is subject to the terms and conditions of the
 *     subcomponent's license, as noted in the LICENSE file.
 *******************************************************************************/
package com.rst.oauth2.google.security;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Copied the DefaultAccessTokenConverter and modified for Google token details.
 */
public class GoogleAccessTokenConverter extends DefaultAccessTokenConverter {

    private UserAuthenticationConverter userTokenConverter;

    public GoogleAccessTokenConverter() {
        setUserTokenConverter(new DefaultUserAuthenticationConverter());
    }

    /**
     * Converter for the part of the data in the token representing a user.
     *
     * @param userTokenConverter the userTokenConverter to set
     */
    public final void setUserTokenConverter(UserAuthenticationConverter userTokenConverter) {
        this.userTokenConverter = userTokenConverter;
        super.setUserTokenConverter(userTokenConverter);
    }

    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        Map<String, String> parameters = new HashMap<>();
        Set<String> scope = parseScopes(map);
        Authentication user = userTokenConverter.extractAuthentication(map);
        String clientId = (String) map.get(CLIENT_ID);
        parameters.put(CLIENT_ID, clientId);
        Set<String> resourceIds = new LinkedHashSet<>(map.containsKey(AUD) ? (Collection<String>) map.get(AUD) : Collections.<String>emptySet());
        OAuth2Request request = new OAuth2Request(parameters, clientId, null, true, scope, resourceIds, null, null, null);
        return new OAuth2Authentication(request, user);
    }

    private Set<String> parseScopes(Map<String, ?> map) {
        // Parsing of scopes coming back from Google are slightly different from the default implementation
        // Instead of it being a collection it is a String where multiple scopes are separated by a space.
        Object scopeAsObject = map.containsKey(SCOPE) ? map.get(SCOPE) : EMPTY;
        Set<String> scope = new LinkedHashSet<>();
        if (String.class.isAssignableFrom(scopeAsObject.getClass())) {
            String scopeAsString = (String) scopeAsObject;
            Collections.addAll(scope, scopeAsString.split(" "));
        } else if (Collection.class.isAssignableFrom(scopeAsObject.getClass())) {
            Collection<String> scopes = (Collection<String>) scopeAsObject;
            scope.addAll(scopes);
        }
        return scope;
    }
}