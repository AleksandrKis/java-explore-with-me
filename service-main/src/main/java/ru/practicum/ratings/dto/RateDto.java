package ru.practicum.ratings.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

// for out some controllers.
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RateDto {
    Long likes; // count rating true;
    Long disLikes; // count rating false;

    @Override
    public String toString() {
        return "RateDto{" +
                "Likes=" + likes +
                ", DisLikes=" + disLikes +
                '}';
    }
}
