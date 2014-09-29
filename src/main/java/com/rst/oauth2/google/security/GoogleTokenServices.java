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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Copied from Spring Security OAuth2 to support the custom format for a Google Token which is different from what Spring supports
 */
public class GoogleTokenServices extends RemoteTokenServices {

    private static Logger LOGGER = LoggerFactory.getLogger(GoogleTokenServices.class);

    private RestOperations restTemplate;

    private String checkTokenEndpointUrl;

    private String clientId;

    private String clientSecret;

    private AccessTokenConverter tokenConverter = new GoogleAccessTokenConverter();

    public GoogleTokenServices() {
        restTemplate = new RestTemplate();
        ((RestTemplate) restTemplate).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            // Ignore 400
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }

    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setCheckTokenEndpointUrl(String checkTokenEndpointUrl) {
        this.checkTokenEndpointUrl = checkTokenEndpointUrl;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
        this.tokenConverter = accessTokenConverter;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = new TokenRequest(accessToken).checkToken();

        LOGGER.info("map1 = {}", map);
        map.put("client_id", map.get("issued_to"));
        map.put("user_name", map.get("user_id"));
        map.put("authorities", "DOMAIN_USER,REGISTERED_USER");
        LOGGER.info("map2 = {}", map);

        Assert.state(map.containsKey("client_id"), "Client id must be present in response from auth server");
        return tokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }


    private Map<String, Object> postForMap(String path, MultiValueMap<String, String> formData, HttpHeaders headers) {
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        return restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<>(formData, headers), Map.class).getBody();
    }

    private class TokenRequest {
        private String accessToken;
        private MultiValueMap<String, String> formData;
        private HttpHeaders headers;
        private String accessTokenInUrl;

        public TokenRequest(String accessToken) {
            this.accessToken = accessToken;
        }

        private TokenRequest prepare() {
            formData = new LinkedMultiValueMap<>();
            formData.add("token", accessToken);
            headers = new HttpHeaders();
            headers.set("Authorization", getAuthorizationHeader(clientId, clientSecret));
            accessTokenInUrl = "?access_token=" + accessToken;
            return this;
        }

        private String getAuthorizationHeader(String clientId, String clientSecret) {
            String creds = String.format("%s:%s", clientId, clientSecret);
            try {
                return "Basic " + new String(Base64.encode(creds.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("Could not convert String");
            }
        }

        private Map<String, Object> checkToken() {
            prepare();
            Map<String, Object> map = postForMap(checkTokenEndpointUrl + accessTokenInUrl, formData, headers);
            if (map.containsKey("error")) {
                logger.debug("check_token returned error: " + map.get("error"));
                throw new InvalidTokenException(accessToken);
            }
            return map;
        }
    }
}
