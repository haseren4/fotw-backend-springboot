// src/main/java/wis/fotabackend/config/SecurityConfig.java
package wis.fotabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // preflight
                        .requestMatchers("/api/**").permitAll()                  // <-- open all API in dev
                        .anyRequest().denyAll()
                )
                .httpBasic(httpBasic -> httpBasic.disable())               // no auth prompts
                .formLogin(form -> form.disable());                        // no redirects
        return http.build();
    }
}
