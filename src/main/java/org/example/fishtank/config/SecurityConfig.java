package org.example.fishtank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * Problem concerning csrf: A post request can be denied with 403 forbidden if spring security can not assure safe posting.
 * The problem can be fixed with thymeleaf th:action="@{url} instead of action="url", which authenticate the post.
 * ex. th:action="@{/add_fish}"
 * The problem can also be fixed by disabling the csrf in security config.
 * https://stackoverflow.com/questions/50486314/how-to-solve-403-error-in-spring-boot-post-request
 *
 * /logout works when .csrf is disabled. Redirects to /login?logout when no logout is defined. Logs out the user.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                //.csrf(csrf -> csrf.ignoringRequestMatchers("/my_fishes/images/upload"))
                .csrf(AbstractHttpConfigurer::disable)

                //.oauth2Login(Customizer.withDefaults()) // sets up the login for oauth2 login with default login page and settings
                //.formLogin(Customizer.withDefaults()) // sets up the login for form login with default login page and settings
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .permitAll())
                
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .permitAll())

                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers("/login").permitAll(); // permit all application users to get to url: localhost:8080/login
                    authorizeRequests.requestMatchers("/signup").permitAll(); // permit all application users to get to url: localhost:8080/login
                    //authorizeRequests.requestMatchers("/logout").permitAll();
                    authorizeRequests.requestMatchers("/my_fishes/images/**").permitAll();
                    authorizeRequests.anyRequest().authenticated(); // any other request can only be reached by an authenticated user.
                })

                .build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
