package project.doblog.domain.user.repository;

import org.springframework.data.repository.CrudRepository;
import project.doblog.infra.security.jwt.token.RefreshToken;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    RefreshToken findByRefreshToken(String refreshToken);
}
