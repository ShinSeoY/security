package security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import security.domain.MyUser;
import security.dto.LoginDto;
import security.exception.error.InvalidValueException;
import security.repository.UserRepository;
import security.security.AuthenticationTokenProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationTokenProvider authenticationTokenProvider;

    public MyUser createUser(MyUser myUser) {
        if (myUser.getUsername() != null && myUser.getPassword() != null) {
            return userRepository.save(myUser);
        }
        return null;
    }

    public String login(LoginDto loginDto) {
        MyUser myUser = userRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
        if (myUser != null) {
            String jwtToken = authenticationTokenProvider.generateJwtToken(myUser);
            return jwtToken;
        } else {
            throw new InvalidValueException();
        }
    }


}
