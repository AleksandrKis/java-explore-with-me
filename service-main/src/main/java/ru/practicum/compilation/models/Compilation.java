package ru.practicum.compilation.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.events.models.Event;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "pinned", columnDefinition = "boolean default false")
    Boolean pinned;
    @Column(name = "title", nullable = false)
    String title;
    @ManyToMany
    @JoinTable(name = "compilation_events",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    @ToString.Exclude
    Set<Event> events;
}