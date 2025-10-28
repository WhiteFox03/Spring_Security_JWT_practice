package lacshery.service;

import lacshery.dto.UserDto;
import lacshery.dto.jwtToken.JwtAuthenticationDto;
import lacshery.dto.jwtToken.RefreshTokenDto;
import lacshery.dto.jwtToken.UserCredentialsDto;
import lacshery.mapper.UserMapper;
import lacshery.model.User;
import lacshery.repository.UserRepository;
import lacshery.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generatorAuthToken(user.getEmail());
    }

    @Override
    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
         if(refreshToken != null && jwtService.validateJwtToken(refreshToken)){
             User user = userRepository.findByEmail(jwtService.getEmailFromToken(refreshToken))
                     .orElseThrow(() -> new ArithmeticException("пользователя с таким email не найдено!"));
             return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
         }
         throw new ArithmeticException("invalid refresh token");
    }

    @Override
    public String create(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует!");
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "add User";
    }

    @Override
    public UserDto getUser(long id) {
        User result = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Такого пользователя не существует"));
        return userMapper.toDto(result);
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<User> userOptional = userRepository.findByEmail(userCredentialsDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }
}