package security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import security.domain.MyUser;
import security.repository.UserRepository;
import security.security.AuthenticationTokenProvider;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthenticationTokenProvider authenticationTokenProvider;

    public MyUser createUser(MyUser myUser){
        if(myUser.getUsername() != null && myUser.getPassword() != null){
            return userRepository.save(myUser);
        }
        return null;
    }

    public String login(Map<String, Object> loginDto){
        MyUser myUser = userRepository.findByUsernameAndPassword(String.valueOf(loginDto.get("username")), String.valueOf(loginDto.get("password")));
        if(myUser != null){
            return authenticationTokenProvider.generateJwtToken(myUser);
        } else {
            return "NONE_MATCH_USER";
        }
    }


}
