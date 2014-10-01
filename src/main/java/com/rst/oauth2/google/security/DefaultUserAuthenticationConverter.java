/*
 * Cloud Foundry 2012.02.03 Beta
 * Copyright (c) [2009-2012] VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product includes a number of subcomponents with
 * separate copyright notices and license terms. Your use of these
 * subcomponents is subject to the terms and conditions of the
 * subcomponent's license, as noted in the LICENSE file.
 */

package com.rst.oauth2.google.security;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Copied from the original implementation of the <code>DefaultUserAuthenticationConverter</code> to fix a bug in the
 * <code>getAuthorities</code> method. Rest all unchanged. Class with the original bug
 * <code>org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter</code>
 *
 * @author Dave Syer
 */
public class DefaultUserAuthenticationConverter extends org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter {

    private Collection<? extends GrantedAuthority> defaultAuthorities;

    private AuthorityGranter authorityGranter;

    /**
     * Default value for authorities if an Authentication is being created and the input has no data for authorities.
     * Note that unless this property is set, the default Authentication created by {@link #extractAuthentication(java.util.Map)}
     * will be unauthenticated.
     *
     * @param defaultAuthorities the defaultAuthorities to set. Default null.
     */
    public void setDefaultAuthorities(String[] defaultAuthorities) {
        this.defaultAuthorities = commaSeparatedStringToAuthorityList(arrayToCommaDelimitedString(defaultAuthorities));
    }

    /**
     * Authority granter which can grant additional authority to the user based on custom rules.
     *
     * @param authorityGranter
     */
    public void setAuthorityGranter(AuthorityGranter authorityGranter) {
        this.authorityGranter = authorityGranter;
    }

    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            return new UsernamePasswordAuthenticationToken(map.get(USERNAME), "N/A", getAuthorities(map));
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        List<GrantedAuthority> authorityList = newArrayList();
        if (!map.containsKey(AUTHORITIES)) {
            assignDefaultAuthorities(authorityList);
        } else {
            grantAuthoritiesBasedOnValuesInMap(map, authorityList);
        }
        grantAdditionalAuthorities(map, authorityList);
        return authorityList;
    }

    private void grantAuthoritiesBasedOnValuesInMap(Map<String, ?> map, List<GrantedAuthority> authorityList) {
        List<GrantedAuthority> parsedAuthorities = parseAuthorities(map);
        authorityList.addAll(parsedAuthorities);
    }

    private void grantAdditionalAuthorities(Map<String, ?> map, List<GrantedAuthority> authorityList) {
        if (authorityGranter != null) {
            authorityList.addAll(authorityGranter.getAuthorities(map));
        }
    }

    private void assignDefaultAuthorities(List<GrantedAuthority> authorityList) {
        if (defaultAuthorities != null) {
            authorityList.addAll(defaultAuthorities);
        }
    }

    private List<GrantedAuthority> parseAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        List<GrantedAuthority> parsedAuthorities;
        if (authorities instanceof String) {
            // Bugfix for Spring OAuth codebase
            parsedAuthorities = commaSeparatedStringToAuthorityList((String) authorities);
        } else if (authorities instanceof Collection) {
            parsedAuthorities = commaSeparatedStringToAuthorityList(collectionToCommaDelimitedString((Collection<?>) authorities));
        } else {
            throw new IllegalArgumentException("Authorities must be either a String or a Collection");
        }
        return parsedAuthorities;
    }
}
