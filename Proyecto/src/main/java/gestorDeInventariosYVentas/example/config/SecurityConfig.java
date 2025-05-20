package gestorDeInventariosYVentas.example.config;

import gestorDeInventariosYVentas.example.config.filter.JwtTokenValidator;
import gestorDeInventariosYVentas.example.service.impl.UserDetailsServiceImpl;
import gestorDeInventariosYVentas.example.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private  JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .csrf(csrf ->csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    //Configurar los endpoints publicos
                    http.requestMatchers("/api/v1/auth/**").permitAll();
                    http.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    //Configurar los endpoints privados
                    http.requestMatchers("/api/v1/categories/**").hasAnyRole("ADMIN","SALES_MANAGER");
                    http.requestMatchers("/api/v1/customers/**").hasAnyRole("ADMIN","CASHIER","SALES_MANAGER");
                    http.requestMatchers("/api/v1/orders-details/**").hasAnyRole("ADMIN","CASHIER","SALES_MANAGER");
                    http.requestMatchers("/api/v1/orders/**").hasAnyRole("ADMIN","CASHIER","SALES_MANAGER");
                    http.requestMatchers("/api/v1/products/**").hasAnyRole("ADMIN","SALES_MANAGER");

                    http.anyRequest().denyAll();
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
