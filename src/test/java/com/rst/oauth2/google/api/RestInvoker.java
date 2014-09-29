package com.rst.oauth2.google.api;

import org.apache.commons.codec.binary.Base64;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: saket.kumar
 * Date: 10/09/2014
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
class RestInvoker {
    private final EmbeddedWebApplicationContext server;

    protected RestTemplate restTemplate = new TestRestTemplate();
    private String baseUrl;

    public RestInvoker(EmbeddedWebApplicationContext server) {
        this.server = server;
        baseUrl = "http://localhost:" + this.server.getEmbeddedServletContainer().getPort() + "/";
    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType) {
        HttpEntity<?> requestEntity = createAuthHeaders();
        return restTemplate.exchange(URI.create(baseUrl + url), HttpMethod.GET, requestEntity, responseType);
    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        HttpEntity<?> requestEntity = createAuthHeaders();
        return restTemplate.exchange(URI.create(baseUrl + url), HttpMethod.GET, requestEntity, responseType);
    }

    private HttpEntity<?> createAuthHeaders() {
        MultiValueMap<String, String> headers = createHeaders("user", "password");
        return new HttpEntity<>(headers);
    }

    private HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }
}
