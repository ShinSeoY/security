package security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import security.domain.User;
import security.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user){
        if(user.getUsername() != null && user.getPassword() != null){
            return userRepository.save(user);
        }
        return null;
    }


}
