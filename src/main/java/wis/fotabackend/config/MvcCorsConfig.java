// src/main/java/wis/fotabackend/config/MvcCorsConfig.java
package wis.fotabackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                // Allow production domains (both schemes and with/without www)
                .allowedOrigins(
                        "https://www.fortsontheair.com",
                        "http://www.fortsontheair.com",
                        "https://fortsontheair.com",
                        "http://fortsontheair.com",
                        // Dev origin for Angular CLI
                        "http://localhost:4200"
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)  // Angular uses withCredentials; allow credentialed requests
                .maxAge(3600);
    }
}
