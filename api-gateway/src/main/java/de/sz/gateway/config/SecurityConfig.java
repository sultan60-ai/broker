package de.sz.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

  @Value("${app.security.disableAuth:false}")
  private boolean disableAuth;

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http.csrf(ServerHttpSecurity.CsrfSpec::disable);

    if (disableAuth) {
      http.authorizeExchange(ex -> ex.anyExchange().permitAll());
      return http.build();
    }

    http.authorizeExchange(ex -> ex
        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/accounts/**").authenticated()
        .pathMatchers("/trading/**").authenticated()
        .anyExchange().authenticated()
    );

    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }
}
