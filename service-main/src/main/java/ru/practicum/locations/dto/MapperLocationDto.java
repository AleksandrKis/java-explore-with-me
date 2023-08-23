package ru.practicum.locations.dto;

import ru.practicum.locations.models.Location;

public class MapperLocationDto {
    public static Location mapToLocation(LocationDto locationDto) {
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat()).build();
    }

    public static LocationDto mapToDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon()).build();
    }
}