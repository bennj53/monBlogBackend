package com.whiterabbit.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("springSecurity");
        //désactiver spring security//////////
        //http.authorizeRequests().antMatchers("/").permitAll();
        //désactiver token csrf et autoriser requetes CORS
        http.cors().and().csrf().disable();
        //spring n'utilisera plus les sessions
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        /////////////////////////////////////////
        //autoriser accès sans aut a ces pages pour des requettes get
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/categories/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/articles/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/article/**").permitAll();
        //TODO: à changer pour la prod
            http.authorizeRequests().antMatchers(HttpMethod.POST,"/articles/**").permitAll();
            http.authorizeRequests().antMatchers(HttpMethod.PUT,"/articles/**").permitAll();
            http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/articles/**").permitAll();
        //
        //role necessaire pour accès aux produits et categories
        http.authorizeRequests().antMatchers("/categories/**").hasAuthority("ADMIN");
        http.authorizeRequests().antMatchers("/articles/**").hasAuthority("USER");
        http.authorizeRequests().antMatchers("/article/**").hasAuthority("USER");
        //autentification requise pour toutes les requetes
        http.authorizeRequests().anyRequest().authenticated();
        //ajout d un filtre d interception requettes http pour check tokken JWT
        http.addFilterBefore(new JWTAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
