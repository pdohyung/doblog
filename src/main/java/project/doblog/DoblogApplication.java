package project.doblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import project.doblog.global.config.JwtConfig;

@EnableConfigurationProperties(JwtConfig.class)
@SpringBootApplication
public class DoblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DoblogApplication.class, args);
    }
}
