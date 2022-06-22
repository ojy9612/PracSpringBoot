package com.homework.hanghae99homework02.repository;

import com.homework.hanghae99homework02.model.Board;
import com.homework.hanghae99homework02.model.Likes;
import com.homework.hanghae99homework02.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes,Long> {

    Optional<Likes> findByBoardAndUser(Board board, User user);

}
