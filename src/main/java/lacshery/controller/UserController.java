package lacshery.controller;

import jakarta.validation.Valid;
import lacshery.dto.UserDto;
import lacshery.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public String create (@RequestBody @Valid UserDto userDto){
        return userService.create(userDto);
    }

    @GetMapping("/users/{id}")
    public UserDto getUser (@PathVariable long id){
        return userService.getUser(id);
    }
}