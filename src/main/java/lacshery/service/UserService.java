package lacshery.service;

import lacshery.dto.UserDto;
import lacshery.dto.jwtToken.JwtAuthenticationDto;
import lacshery.dto.jwtToken.RefreshTokenDto;
import lacshery.dto.jwtToken.UserCredentialsDto;

import javax.security.sasl.AuthenticationException;

public interface UserService {
    JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException;
    JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto);
    String create(UserDto userDto);
    UserDto getUser(long id);
}