package security.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class TokenInfo {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
