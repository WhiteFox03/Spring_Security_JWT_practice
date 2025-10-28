package lacshery.controller;

import lacshery.dto.jwtToken.JwtAuthenticationDto;
import lacshery.dto.jwtToken.RefreshTokenDto;
import lacshery.dto.jwtToken.UserCredentialsDto;
import lacshery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.AuthenticationException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/sing-in")
    public ResponseEntity<JwtAuthenticationDto> singIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthenticationDto JwtAuthenticationDto = userService.singIn(userCredentialsDto);
            return ResponseEntity.ok(JwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed " + e.getMessage());
        }
    }
    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto){
        return userService.refreshToken(refreshTokenDto);
    }
}