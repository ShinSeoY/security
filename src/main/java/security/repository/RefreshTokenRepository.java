package security.repository;

import org.springframework.data.repository.CrudRepository;
import security.domain.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
