package org.example.fishtank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity

                .oauth2Login(Customizer.withDefaults()) // sets up the login for oauth2 login with default login page and settings
                //.formLogin(Customizer.withDefaults()) // sets up the login for form login with default login page and settings

                // sets up custom login page
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .loginPage("/login")
//                        .permitAll())
//
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login")
//                        .permitAll())

                .authorizeHttpRequests(authorizeRequests -> {
                    //authorizeRequests.requestMatchers("/").permitAll(); // permit all application users to get to url: localhost:8080/
                    authorizeRequests.requestMatchers("/login").permitAll(); // permit all application users to get to url: localhost:8080/login
                    //authorizeRequests.requestMatchers("/signup").permitAll(); // permit all application users to get to url: localhost:8080/signup
                    authorizeRequests.anyRequest().authenticated(); // any other request can only be reached by an authenticated user.
                })

                .build();
    }
}
