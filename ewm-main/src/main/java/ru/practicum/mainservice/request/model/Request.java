package ru.practicum.mainservice.request.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Request {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "event_id")
    Event event;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id")
    User requester;

    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    Status status;
}
