package security.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.domain.MyUser;
import security.repository.UserRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

//  public UserDetails loadUserByUsernameId(UsernameId usernameId) throws UsernameNotFoundException {
//    //Optional<AuthUser> found = userRepository.findByUsername(username, route);
//    Optional<AuthUser> found = userRepository.findAuthUserByUsername(usernameId);
//    AuthUser person = found.orElseThrow(() -> new UsernameNotFoundException(String.format("User %s does not exist!", usernameId)));
//    return new MyUserDetails(person.getUsername().getUsername(), "", loadAuthorities(person.getId()), person);
//  }
//
//  private Collection<GrantedAuthority> loadAuthorities(Long userId) {
//    Collection<Role> userAuthorities = userRepository.findRole(userId);
//    return userAuthorities.stream().map(it -> new SimpleGrantedAuthority(it.getAuthority())).collect(Collectors.toList());
//  }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> found = userRepository.findMyUserByUsername(username);
        MyUser myUser = found.orElseThrow(() -> new UsernameNotFoundException(String.format("User %s does not exist!", username)));
        return new MyUserDetails(myUser);
    }
}
