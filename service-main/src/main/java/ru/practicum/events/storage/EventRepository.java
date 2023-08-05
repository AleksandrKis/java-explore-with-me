package ru.practicum.events.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.models.Event;
import ru.practicum.events.models.EventStatus;
import ru.practicum.events.models.SortEvent;
import ru.practicum.users.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Page<Event>> findAllByInitiator(User initiator, Pageable page);

    @Query("SELECT e FROM Event e " +
            "WHERE (:userId IS NULL OR e.initiator.id IN :userId) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories)" +
            "AND (e.eventDate BETWEEN cast(:rangeStart as date) AND cast(:rangeEnd as date))")
    Page<Event> findEventsByAdmin(@Param("userId") Set<Long> userId,
                                  @Param("states") Set<EventStatus> states,
                                  @Param("categories") Set<Long> categories,
                                  @Param("rangeStart") LocalDateTime rangeStart,
                                  @Param("rangeEnd") LocalDateTime rangeEnd,
                                  Pageable page);

    @Query("SELECT e FROM Event e WHERE (:text IS NULL " +
            "OR LOWER(e.annotation) LIKE LOWER(concat('%', :text, '%'))" +
            "OR LOWER(e.description) LIKE LOWER(concat('%', :text, '%'))" +
            "OR LOWER(e.title) LIKE LOWER(concat('%', :text, '%')))" +
            "AND (:categories IS NULL OR e.category.id IN :categories)" +
            "AND (:paid IS NULL OR e.paid = :paid)" +
            "AND (e.state = 'PUBLISHED')" +
            "AND (e.eventDate BETWEEN cast(:start as date) AND cast(:rangeEnd as date))" +
            "ORDER BY :sort")
    List<Event> findEventsByPublic(@Param("text") String text,
                                   @Param("categories") Set<Long> categories,
                                   @Param("paid") Boolean paid,
                                   @Param("start") LocalDateTime start,
                                   @Param("rangeEnd") LocalDateTime rangeEnd,
                                   @Param("sort") SortEvent sort,
                                   Pageable page);
}