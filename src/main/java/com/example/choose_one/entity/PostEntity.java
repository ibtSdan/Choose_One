package com.example.choose_one.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "post")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PostEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "content_a")
    private String contentA;

    @Column(name = "content_b")
    private String contentB;

    @ManyToOne
    @JsonIgnore
    @ToString.Exclude
    private UserEntity user;

    @OneToMany(mappedBy = "post")
    private List<VoteEntity> voteList = List.of();

}
