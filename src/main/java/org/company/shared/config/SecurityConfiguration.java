package org.company.shared.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
//                                .anyRequest().permitAll()
                                .requestMatchers("api/auth/**").permitAll()

                                .requestMatchers("api/users/**").hasAnyAuthority("ADMIN", "USER", "PARTNER", "COURIER") //user only for himself - will check elsewhere
                                .requestMatchers("api/partners/reviews/**").hasAnyAuthority("ADMIN", "USER", "PARTNER", "COURIER")
                                .requestMatchers(HttpMethod.GET, "/api/partners/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/partners/**").hasAnyAuthority("PARTNER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/partners/**").hasAnyAuthority("PARTNER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/partners/**").hasAnyAuthority("PARTNER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/partners/photo/**").permitAll()

                                .requestMatchers("api/delivery_mans/**").hasAuthority("ADMIN")

                                .requestMatchers("api/addresses/**").hasAuthority("ADMIN")

                                .requestMatchers(HttpMethod.GET, "/api/products/photo/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyAuthority("PARTNER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("PARTNER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("PARTNER", "ADMIN")

                                .requestMatchers("api/categories/**").hasAnyAuthority("ADMIN", "PARTNER")
                                .requestMatchers("api/categories").hasAnyAuthority("ADMIN", "PARTNER")



                                .requestMatchers(HttpMethod.GET, "/api/orders/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/orders/rating/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/orders/assign/**").hasAnyAuthority("COURIER", "PARTNER")
                                .requestMatchers(HttpMethod.GET, "/api/orders/partner/**").hasAnyAuthority("PARTNER")
                                .requestMatchers(HttpMethod.GET, "/api/orders/assigned/**").hasAnyAuthority("COURIER")
                                .requestMatchers(HttpMethod.PUT, "/api/orders/set-status/**").hasAnyAuthority("COURIER", "PARTNER")
                                .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyAuthority("USER", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyAuthority("PARTNER", "COURIER", "ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasAnyAuthority( "ADMIN")

                                .requestMatchers("api/product_order/**").hasAnyAuthority("ADMIN", "USER", "PARTNER")
                                .requestMatchers("api/recurring_orders/**").hasAnyAuthority("ADMIN", "USER", "PARTNER")
                                .requestMatchers("api/recurring_orders/owning/**").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("api/recurring_orders/**").hasAnyAuthority("ADMIN", "USER", "PARTNER")
                                .requestMatchers(HttpMethod.DELETE,"api/recurring_orders/**").hasAnyAuthority("ADMIN", "USER")
                        
                                .requestMatchers(HttpMethod.POST,"/api/complaints/**").hasAnyAuthority("USER", "ADMIN", "PARTNER", "COURIER")
                                .requestMatchers("/api/complaints/**").hasAnyAuthority("ADMIN")
                )
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
     
        return http.build();
    }
}
