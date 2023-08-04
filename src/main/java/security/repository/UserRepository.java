package security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
