package org.example.fishtank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Problem concerning csrf: A post request can be denied with 403 forbidden if spring security can not assure safe posting.
 * The problem can be fixed with thymeleaf th:action="@{url} instead of action="url", which authenticate the post.
 * ex. th:action="@{/add_fish}"
 * The problem can also be fixed by disabling the csrf in security config.
 * https://stackoverflow.com/questions/50486314/how-to-solve-403-error-in-spring-boot-post-request
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        return httpSecurity
                //.oauth2Login(Customizer.withDefaults()) // sets up the login for oauth2 login with default login page and settings
                //.formLogin(Customizer.withDefaults()) // sets up the login for form login with default login page and settings

                // sets up custom login page
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .permitAll())
                .csrf(csrf -> csrf.ignoringRequestMatchers("/my_fishes/images/upload"))

               /* .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll())*/

                .authorizeHttpRequests(authorizeRequests -> {
                    //authorizeRequests.requestMatchers("/").permitAll(); // permit all application users to get to url: localhost:8080/
//                    authorizeRequests.requestMatchers("/login").permitAll(); // permit all application users to get to url: localhost:8080/login
//                    authorizeRequests.requestMatchers("/signup").permitAll();
//                    authorizeRequests.requestMatchers("/home").permitAll();
                    authorizeRequests.requestMatchers("/my_fishes/images/**").authenticated();
                      authorizeRequests.requestMatchers("/").permitAll();


                    // Test this to see if login works


// permit all application users to get to url: localhost:8080/signup
                    authorizeRequests.anyRequest().permitAll(); // any other request can only be reached by an authenticated user.
                })
                .build();
    }
}


