package com.rst.oauth2.google.api;

import static com.google.common.collect.Lists.newArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rst.oauth2.google.security.GoogleProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Component
@RequestMapping("find")
public class UserResource {
    private static Logger LOGGER = LoggerFactory.getLogger(UserResource.class);
    private static ObjectMapper OM = new ObjectMapper();
    @Autowired private OAuth2RestOperations oauth2RestTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<String> findUsersStartingWithPrefix(@RequestParam("term") String usernamePrefix) throws JsonProcessingException {
        LOGGER.info("Searching for users starting with {}", usernamePrefix);
        List<String> list = newArrayList("user@email.com", "user2@email.com");

        GoogleProfile profile = getGoogleProfile();
        LOGGER.info("Google Profile Data {}", OM.writerWithDefaultPrettyPrinter().writeValueAsString(profile));
        list.add(profile.getEmail());

        LOGGER.info("Returning users {}", list);
        return list;
    }

    private GoogleProfile getGoogleProfile() {
        String url = "https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + oauth2RestTemplate.getAccessToken();
        ResponseEntity<GoogleProfile> forEntity = oauth2RestTemplate.getForEntity(url, GoogleProfile.class);
        return forEntity.getBody();
    }
}
