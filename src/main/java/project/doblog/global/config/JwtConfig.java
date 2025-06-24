package project.doblog.global.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.SecretKey;

@Slf4j
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private final SecretKey key;
    private final long accessExpirations;
    private final long refreshExpirations;

    public JwtConfig(String secret, long accessExpirations, long refreshExpirations) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.accessExpirations = accessExpirations;
        this.refreshExpirations = refreshExpirations;
    }
}
