package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequestDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.models.User;

import java.util.List;

public interface UserService {
    UserDto addNewUser(NewUserRequestDto newUserRequestDto);

    List<UserDto> getUsers(int from, int size);

    List<UserDto> getUsers(List<Long> ids, int from, int size);

    void deleteUser(Long id);

    User checkUserExist(Long id);
}