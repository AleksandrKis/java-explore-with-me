package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exceptions.UserNotFoundException;
import ru.practicum.users.dto.NewUserRequestDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.models.User;
import ru.practicum.users.storage.UserRepository;

import java.util.List;

import static ru.practicum.users.dto.MapperUserDto.*;
import static ru.practicum.utility.Constant.getPage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    @Override
    public UserDto addNewUser(NewUserRequestDto newUserRequestDto) {
        return mapToDto(userRepo.save(mapToUser(newUserRequestDto)));
    }

    @Override
    public List<UserDto> getUsers(int from, int size) {
        return mapToListDto(userRepo.findAll(getPage(from, size)).toList());
    }

    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        return mapToListDto(userRepo.findByIdIn(ids, getPage(from, size)));
    }

    @Override
    public void deleteUser(Long id) {
        userRepo.delete(checkUserExist(id));
    }

    @Override
    public User checkUserExist(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("Not found User with id:" + id));
    }
}