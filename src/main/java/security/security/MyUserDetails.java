package security.security;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import security.domain.MyUser;

import java.util.List;

@Getter
public class MyUserDetails extends User {

    private MyUser myUser;

    public MyUserDetails(MyUser myUser) {
        super(myUser.getUsername(), myUser.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        this.myUser = myUser;
    }
}
