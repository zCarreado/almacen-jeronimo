package com.ciclomotos.ciclomotos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      /*  http.csrf().disable()
            .authorizeRequests()
            .anyRequest().permitAll();
        return http.build();
   */ 
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/images/**").permitAll() // públicas
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout.permitAll());
        return http.build();
    }
}
