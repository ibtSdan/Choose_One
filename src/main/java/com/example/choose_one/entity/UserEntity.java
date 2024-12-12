package com.example.choose_one.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;

    private String role;

    @OneToMany(mappedBy = "user")
    private List<PostEntity> postList = List.of();

    @OneToMany(mappedBy = "user")
    private List<VoteEntity> voteList = List.of();
}
