package lacshery.dto.jwtToken;

import lombok.Data;

@Data
public class UserCredentialsDto {
    private String email;
    private String password;
}