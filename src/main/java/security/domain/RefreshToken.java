package security.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@RedisHash(value = "refreshToken")
public class RefreshToken {

    @Id // import org.springframework.data.annotation.Id;
    private String refreshToken;

    @Indexed // repository findBy 작업 수행을 위해
    private String username;

}
