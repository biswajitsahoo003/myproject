package com.tcl.dias.oms.config;

import com.google.common.collect.ObjectArrays;
import com.tcl.dias.common.constants.UserActionsConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

/**
 * This file contains the SecurityConfig.java class. This class have all the
 * security configs
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends SecurityConfigUtils {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .sessionAuthenticationStrategy(sessionAuthenticationStrategy()).and()
                .addFilterBefore(keycloakPreAuthActionsFilter(), LogoutFilter.class)
                .addFilterBefore(keycloakAuthenticationProcessingFilter(), X509AuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and().authorizeRequests()
                .antMatchers("/v1/cof/**")
                .hasAnyAuthority(ObjectArrays.concat(UserActionsConstants.CUSTOMER_ACCESS_GROUP,
                        UserActionsConstants.PARTNER_ACCESS_GROUP, String.class))
                .antMatchers("/isv/**")
                .hasAnyAuthority(ObjectArrays.concat(UserActionsConstants.INTERNAL_ACCESS_GROUP,
                        UserActionsConstants.COMMERCIAL_ACCESS_GROUP, String.class))
                .antMatchers("/v1/**")
                .hasAnyAuthority(ObjectArrays.concat(UserActionsConstants.CUSTOMER_ACCESS_GROUP,
                        UserActionsConstants.PARTNER_ACCESS_GROUP, String.class))
                .antMatchers("/lr/**").authenticated().and().authorizeRequests();
    }
}
