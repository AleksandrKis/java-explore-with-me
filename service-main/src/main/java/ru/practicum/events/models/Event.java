package ru.practicum.events.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.categories.models.Category;
import ru.practicum.compilation.models.Compilation;
import ru.practicum.locations.models.Location;
import ru.practicum.ratings.models.Rate;
import ru.practicum.requests.models.ParticipantRequest;
import ru.practicum.users.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "annotation", nullable = false)
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    List<ParticipantRequest> requests;
    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;
    @Column(name = "description", nullable = false)
    String description;
    @Column(name = "EVENT_DATE", nullable = false)
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;
    @OneToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;
    @Column(name = "paid", columnDefinition = "boolean default false")
    Boolean paid;
    @Column(name = "participant_limit", columnDefinition = "integer default 0")
    Long participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation", columnDefinition = "boolean default true")
    Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    EventStatus state;
    @Column(name = "title", nullable = false)
    String title;
    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    Set<Compilation> compilations;
    @Column(name = "views")
    Long views;
    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    List<Rate> rating;
}