package project.doblog.domain.user;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

@Builder
@Getter
@RedisHash(value = "refresh", timeToLive = 86400)
public class RefreshToken {

    @Indexed
    private String refreshToken;

//    @Id
    private Long id;

//    private Collection<? extends GrantedAuthority> authorities;

//    public String getAuthority() {
//        return authorities.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
//                .toList()
//                .get(0)
//                .getAuthority();
//    }
}
