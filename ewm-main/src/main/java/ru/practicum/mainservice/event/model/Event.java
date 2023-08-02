package ru.practicum.mainservice.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.category.model.Category;
import ru.practicum.mainservice.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_PATTERN;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    Category category;

    @Column
    String description;

    @Column
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime eventDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    Location location;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
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
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime createdOn;

    @Column
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    LocalDateTime publishedOn;

    @Column
    @Enumerated(EnumType.STRING)
    State state;
}
