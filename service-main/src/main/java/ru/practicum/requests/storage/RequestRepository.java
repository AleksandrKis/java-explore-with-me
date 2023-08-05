package ru.practicum.requests.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.models.Event;
import ru.practicum.requests.models.ParticipantRequest;
import ru.practicum.requests.models.RequestStatus;
import ru.practicum.users.models.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipantRequest, Long> {
    Boolean existsByEventAndRequester(Event event, User user);

    List<ParticipantRequest> findAllByRequester(User user);

    Optional<ParticipantRequest> findById(long requestId);

    List<ParticipantRequest> findAllByEvent(Event event);

    List<ParticipantRequest> findAllByIdIn(List<Long> requestIds);

    Long countByEventAndStatus(Event event, RequestStatus status);

    List<ParticipantRequest> findAllByEventAndStatus(Event event, RequestStatus status);

    @Query("SELECT pr FROM ParticipantRequest pr WHERE pr.event = ?1 AND pr.status IN (?2)")
    List<ParticipantRequest> findAllByEventAndStatus(Event event, Collection<RequestStatus> statuses);
}