package ru.practicum.mainservice.user.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column
    String name;

    @Column
    String email;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "subscriptions",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "subscribed_to_id"))
    List<User> subscriptions;
}
