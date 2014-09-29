package com.rst.oauth2.google.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created with IntelliJ IDEA.
 * User: saket.kumar
 * Date: 22/09/2014
 * Time: 11:57
 * To change this template use File | Settings | File Templates.
 */
@Order(1)
@Configuration
@ImportResource({"classpath:security-context.xml"})
public class SecurityWebAppInitializer extends AbstractSecurityWebApplicationInitializer {
}
