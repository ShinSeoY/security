package security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import security.domain.User;
import security.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class MainController {

    private final UserService userService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public String index() {
        log.info("--------come");
        boolean isShow = true;
        if (isShow) {
            return "success";
        } else {
            throw new RuntimeException("exception...");
        }
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.OK)
    public User createUser(@RequestBody Map<String, Object> user) {
        return userService.createUser(
                User.builder()
                        .username((user.get("username")).toString())
                        .password((user.get("password")).toString())
                        .build()
        );
    }
}
