package security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import security.domain.MyUser;

public interface UserRepository extends JpaRepository<MyUser, Long> {

    @Query("SELECT mu FROM MyUser mu WHERE mu.username = :username AND mu.password = :password")
    MyUser findByUsernameAndPassword(String username, String password);

}
