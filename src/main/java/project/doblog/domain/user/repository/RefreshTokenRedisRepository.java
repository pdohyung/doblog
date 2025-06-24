package project.doblog.domain.user.repository;

import org.springframework.data.repository.CrudRepository;
import project.doblog.domain.user.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
