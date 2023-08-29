package ru.practicum.ratings.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.models.Event;
import ru.practicum.ratings.models.Rate;
import ru.practicum.users.models.User;

import java.util.List;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {

    Optional<Rate> findFirstByEventAndRater(Event event, User rater);

    @Query("SELECT r1.event.id FROM Rate r1 WHERE r1.rate = true " +
            "GROUP BY r1.event.id HAVING COUNT (r1) > 0 AND COUNT (r1) > " +
            "(SELECT COUNT (r2) FROM Rate r2 WHERE r2.rate = false AND r2.event = r1.event) " +
            "ORDER BY COUNT (r1) DESC ")
    List<Long> findAllByEventRating();

    List<Rate> findAllByEventAndRate(Event event, Boolean rate, Pageable page);

    List<Rate> findAllByRaterAndRate(User user, Boolean rate, Pageable page);
}