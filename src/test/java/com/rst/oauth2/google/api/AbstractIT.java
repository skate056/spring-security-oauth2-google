package com.rst.oauth2.google.api;

import com.rst.oauth2.google.config.SpringBootRestApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootRestApplication.class)
@IntegrationTest
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class AbstractIT {
    @Autowired
    protected EmbeddedWebApplicationContext server;
    private RestInvoker restInvoker;

    @Before
    public void setUp(){
        restInvoker = new RestInvoker(server);
    }

    <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restInvoker.get(url, responseType);
    }

    <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType) {
        return restInvoker.get(url, responseType);
    }
}