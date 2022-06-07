package com.leords.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    
    private static final String[] PUBLIC_ENDPOINTS = {
            "/products",
            "/oauth2/token",
            "/h2-console/**"
    };
    
    private static final String[] OPERATOR_OR_ADMIN_ENDPOINTS = {
            "/products/**",
            "/categories/**",
    };
    private static final String[] ADMIN = {
            "/users/**"
    };
    
    @Autowired
    private Environment env;
    @Autowired
    private JwtTokenStore jwtTokenStore;
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(jwtTokenStore);
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (Arrays.asList(env.getActiveProfiles()).contains("test"))
            http.headers().frameOptions().disable();
        
        http.authorizeRequests()
                .antMatchers(PUBLIC_ENDPOINTS).permitAll()
                .antMatchers(HttpMethod.GET, OPERATOR_OR_ADMIN_ENDPOINTS).permitAll()
                .antMatchers(OPERATOR_OR_ADMIN_ENDPOINTS).hasAnyRole("OPERATOR", "ADMIN")
                .antMatchers(ADMIN).hasRole("ADMIN")
                .anyRequest().authenticated();
    }
}
