package com.rst.oauth2.google.api;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserResourceIT extends AbstractIT {

    @Test
    public void shouldIssueARedirect() {
        ResponseEntity<String> entity = get("/find?term=hello", String.class);

        assertThat(entity.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(entity.getHeaders().get("Location").get(0), containsString("google"));
    }

    @Test
    @Ignore
    public void shouldLoadContextAndReturnResponseFromSampleResource() {
        ResponseEntity<String> entity = get("/find?term=hello", String.class);

        assertThat(entity.getStatusCode(), is(HttpStatus.OK));
        assertThat(entity.getBody(), containsString("user@email.com"));
    }
}
