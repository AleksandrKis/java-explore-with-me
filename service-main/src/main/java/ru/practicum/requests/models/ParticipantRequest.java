package ru.practicum.requests.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.proxy.HibernateProxy;
import ru.practicum.events.models.Event;
import ru.practicum.users.models.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    RequestStatus status;
    @Column(name = "created")
    LocalDateTime created;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o)
                .getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ParticipantRequest request = (ParticipantRequest) o;
        return getId() != null && Objects.equals(getId(), request.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }
}