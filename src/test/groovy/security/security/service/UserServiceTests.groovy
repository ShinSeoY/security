package security.security.service

import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import security.domain.MyUser
import security.dto.LoginDto
import security.service.UserService

import javax.annotation.Resource

@SpringBootTest
class UserServiceTests {

    private static final Logger log = LoggerFactory.getLogger(UserServiceTests.class)

    @Resource(name = "userService")
    private UserService userService

    @Test
    void contextLoads() {
//        create()
        login()
    }


    private def create() {
        try {
            return userService.createUser(MyUser.builder()
                    .username("user7")
                    .password("pw7")
                    .build())
        } catch (e) {
            log.error(e.toString())
        }
    }

    private String login() {
        try {
            def token = userService.login(LoginDto.builder()
                    .username("sandy1")
                    .password("test1")
                    .build())

            log.info("** token : {}", token)

        } catch (e) {
            log.error(e.toString())
        }
    }

}
