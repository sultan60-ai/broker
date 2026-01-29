package de.sz.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"app.security.disableAuth=true"})
class SmokeTest {
  @Test void contextLoads() {}
}
