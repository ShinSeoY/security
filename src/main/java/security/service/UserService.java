package security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import security.domain.MyUser;
import security.dto.LoginDto;
import security.dto.TokenInfo;
import security.exception.error.InvalidValueException;
import security.repository.UserRepository;
import security.security.AuthenticationTokenProvider;

import javax.servlet.http.HttpServletResponse;

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

    public void login(LoginDto loginDto, HttpServletResponse httpServletResponse) {
        MyUser myUser = userRepository.findByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword());
        if (myUser != null) {
            TokenInfo tokenInfo = authenticationTokenProvider.generateJwtToken(myUser, httpServletResponse);

            System.out.println("tokenInfo......" + tokenInfo);
        } else {
            throw new InvalidValueException();
        }
    }


}
