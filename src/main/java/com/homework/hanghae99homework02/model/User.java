package com.homework.hanghae99homework02.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class User extends Timestamped{

    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();

    public void addLikes(Likes likes){
        likes.setUser(this);
        this.likesList.add(likes);
    }

    public void addBoard(Board board){
        this.boardList.add(board);
        board.setUser(this);
    }

    @Builder
    public User(@NotNull String password,@NotNull List<String> roles,
                @NotNull String email,@NotNull String nickname) {
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.nickname = nickname;
    }

    public User(){}
}