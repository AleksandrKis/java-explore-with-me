package ru.practicum.ratings.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query(value = "SELECT DISTINCT r.rater_id FROM ratings r " +
            "WHERE r.event_id IN (SELECT r1.event_id FROM ratings r1 WHERE r1.rater_id = :userId) " +
            "AND r.rater_id != :userId AND r.rate = (SELECT r2.rate FROM ratings r2 WHERE r2.rater_id = :userId LIMIT 1)",
            nativeQuery = true)
    List<Long> findMatchingRaterIds(@Param("userId") Long userId);

    @Query("SELECT r.event.id, COUNT(r.rate) as s FROM Rate r " +
            "WHERE r.rater.id IN ?1 AND r.rate = true " +
            "AND NOT EXISTS (SELECT 1 FROM Rate r2 WHERE r2.rater = ?2 AND r2.event = r.event) " +
            "GROUP BY r.event.id " +
            "ORDER BY s DESC")
    List<Long> findPredictionEventsIds(List<Long> raters, User user);
}