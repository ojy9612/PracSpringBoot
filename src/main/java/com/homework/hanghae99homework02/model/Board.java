package com.homework.hanghae99homework02.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long board_id;

    @Column
    private String imageLink;

    @JsonIgnore
    @Column
    private String imageKey;

    @Column
    private String content;

    @Column
    private int layout = 1;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Likes> likesList = new ArrayList<>();


    public void addLikes(Likes likes){
        likes.setBoard(this);
        this.likesList.add(likes);
    }

    public Board(String imageLink, String imageKey, String content,int layout, User user) {
        this.imageLink = imageLink;
        this.imageKey = imageKey;
        this.content = content;
        this.layout = layout;
        user.addBoard(this);
    }

    public void update(String imageLink, String imageKey, String content, int layout){
        this.imageLink = imageLink;
        this.imageKey = imageKey;
        this.content = content;
        this.layout = layout;
    }

    public Board(){

    }

}
