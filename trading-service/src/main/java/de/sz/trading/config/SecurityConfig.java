package de.sz.trading.config;

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
  private boolean disableAuth;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable());
    http.headers(h -> h.frameOptions(f -> f.disable())); // H2 console

    if (disableAuth) {
      http.authorizeHttpRequests(a -> a.anyRequest().permitAll());
      return http.build();
    }

    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
        .requestMatchers("/h2/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/v1/**").hasAuthority("SCOPE_trading.read")
        .requestMatchers(HttpMethod.POST, "/v1/**").hasAuthority("SCOPE_trading.write")
        .anyRequest().authenticated()
    );

    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
