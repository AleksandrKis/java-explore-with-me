package ru.practicum.users.dto;

import ru.practicum.users.models.User;

import java.util.ArrayList;
import java.util.List;

public class MapperUserDto {
    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

    public static ShortUserDto mapToShortDto(User user) {
        return ShortUserDto.builder()
                .id(user.getId())
                .name(user.getName()).build();
    }

    public static List<UserDto> mapToListDto(List<User> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        users.forEach(user -> userDtoList.add(mapToDto(user)));
        return userDtoList;
    }

    public static User mapToUser(NewUserRequestDto newUserRequestDto) {
        return User.builder()
                .name(newUserRequestDto.getName())
                .email(newUserRequestDto.getEmail()).build();
    }
}