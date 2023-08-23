package ru.practicum.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequestDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    static final String ANSWER = "UserController: Received request on ";
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody NewUserRequestDto userDto) {
        log.info(ANSWER + "create an new User: {}.", userDto.getName());
        return userService.addNewUser(userDto);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @PositiveOrZero @RequestParam(required = false, defaultValue = "0") int from,
                                  @Positive @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "get List Users");
        return ids == null ? userService.getUsers(from, size) : userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info(ANSWER + "DELETE User by id-{}", userId);
        userService.deleteUser(userId);
    }
}