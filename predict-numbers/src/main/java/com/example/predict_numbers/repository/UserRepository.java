package com.example.predict_numbers.repository;

import com.example.predict_numbers.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Modifying
    @Query("""
    UPDATE User u
    SET u.turns = u.turns - 1
    WHERE u.id = :userId
      AND u.turns > 0
    """)
    int decreaseTurn(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("""
    UPDATE User u
    SET u.score = u.score + 1
    WHERE u.id = :userId
""")
    int increaseScore(@Param("userId") Long userId);


    List<User> findAllByOrderByScoreDescUpdatedAtAscIdAsc(Pageable pageable);

}
