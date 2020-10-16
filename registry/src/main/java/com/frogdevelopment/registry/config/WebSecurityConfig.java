package com.frogdevelopment.registry.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/eureka/**");
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(EndpointRequest.to("info", "health")).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin()
                .and().httpBasic(Customizer.withDefaults());
    }
}
