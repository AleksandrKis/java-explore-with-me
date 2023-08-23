package ru.practicum.locations.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.locations.models.Location;

public interface LocationRepo extends JpaRepository<Location, Long> {
}