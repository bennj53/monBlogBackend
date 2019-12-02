package com.whiterabbit.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //désactive le securité csrf -> échange de tokken
        http.csrf().disable();
        //spring n'utilisera plus les sessions
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //autoriser accès sans aut a ces pages pour des requettes get
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/categories/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/articles/**").permitAll();
        //role necessaire pour accès aux produits et categories
        http.authorizeRequests().antMatchers("/categories/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/articles/**").hasAuthority("USER");
        //autentification requise pour toutes les requetes
        http.authorizeRequests().anyRequest().authenticated();
        //ajout d un filtre d interception requettes http pour check tokken JWT
        http.addFilterBefore(new JWTAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
