package security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.annotation.LoginUser;
import security.domain.MyUser;
import security.dto.LoginDto;
import security.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MainController {

    private final UserService userService;

    @PostMapping("/public/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
            return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }

    @GetMapping("/public/index")
    @ResponseStatus(HttpStatus.OK)
    public Long publicIndex(@LoginUser MyUser myUser) {
        return myUser.getId();
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public MyUser createUser(@RequestBody Map<String, Object> user) {
        return userService.createUser(
                MyUser.builder()
                        .username((user.get("username")).toString())
                        .password((user.get("password")).toString())
                        .build()
        );
    }

}
