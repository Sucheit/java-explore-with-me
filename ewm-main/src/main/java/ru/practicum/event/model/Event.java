package ru.practicum.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {

    @Id
    @Column(name = "event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @Column
    String description;

    @Column
    LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User initiator;

    @Column
    Boolean paid;

    @Column
    Integer participantLimit;

    @Column
    Boolean requestModeration;

    @Column
    String title;

    @Column
    LocalDateTime createdOn;

    @Column
    LocalDateTime publishedOn;

    @Column
    @Enumerated(EnumType.STRING)
    State state;
}
