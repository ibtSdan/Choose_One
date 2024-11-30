package com.example.choose_one.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "vote")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class VoteEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Character voteOption;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private UserEntity user;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private PostEntity post;
}
