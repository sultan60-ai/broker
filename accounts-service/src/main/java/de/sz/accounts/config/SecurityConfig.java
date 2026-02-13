package de.sz.accounts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Value("${app.security.disableAuth:false}")
  boolean disableAuth;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.headers(h -> h.frameOptions(f -> f.disable())); // for H2 console frames

    if (disableAuth) {
      http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
      return http.build();
    }

    http.authorizeHttpRequests(auth -> auth
    	.requestMatchers("/health", "/test/**").permitAll()
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        .requestMatchers("/h2/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/v1/**").hasAuthority("SCOPE_accounts.read")
        .requestMatchers(HttpMethod.POST, "/v1/**").hasAuthority("SCOPE_accounts.write")
        .anyRequest().authenticated()
    );

    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
