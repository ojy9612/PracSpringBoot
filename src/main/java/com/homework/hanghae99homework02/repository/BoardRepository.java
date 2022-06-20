package com.homework.hanghae99homework02.repository;


import com.homework.hanghae99homework02.model.Board;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

//    @Query("select DISTINCT b from Board b join fetch b.likesList")
//    List<Board> findAllFetchJoin();

    @EntityGraph(attributePaths = {"likesList"})
    @Query("select DISTINCT b from Board b")
    List<Board> findAllEntityGraph(Sort sort);

}
