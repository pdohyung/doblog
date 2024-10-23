package project.doblog.infra.security.jwt.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Builder
@Getter
@RedisHash(value = "refresh", timeToLive = 604800)
public class RefreshToken {

    private Long id;
    private String email;
    private Collection<? extends GrantedAuthority> authorities;

    @Indexed
    private String refreshToken;

    public String getAuthority() {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList()
                .get(0)
                .getAuthority();
    }
}
