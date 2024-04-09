package ru.ulstu.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.ulstu.util.jwt.JwtFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {
    //TODO: put delete доступны для manager исправить
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
        return http.headers().frameOptions().sameOrigin().and()
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests((authz) -> authz
                        //.requestMatchers("/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/auth").permitAll()

                        .requestMatchers("/edit-profile", "/me").authenticated()

                        .requestMatchers(HttpMethod.GET,"/user**").hasAnyRole("DEAN", "ADMIN")
                        .requestMatchers("/user**").hasRole("ADMIN")

                        .requestMatchers("/value**").hasAnyRole("DEAN", "MANAGER")
                        .requestMatchers("/calculation**").hasAnyRole("DEAN", "MANAGER")

                        .requestMatchers(HttpMethod.GET,"/opop**").hasAnyRole("DEAN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/opop**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.POST, "/opop**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.DELETE, "/opop**").hasRole("DEAN")

                        .requestMatchers(HttpMethod.GET,"/indicator**").hasAnyRole("DEAN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/indicator**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.POST,"/indicator**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.DELETE,"/indicator**").hasRole("DEAN")

                        .requestMatchers(HttpMethod.GET,"/variable**").hasAnyRole("DEAN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/variable**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.POST,"/variable**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.DELETE,"/variable**").hasRole("DEAN")

                        .requestMatchers(HttpMethod.GET,"/rule**").hasAnyRole("DEAN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT,"/rule**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.POST,"/rule**").hasRole("DEAN")
                        .requestMatchers(HttpMethod.DELETE,"/rule**").hasRole("DEAN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
