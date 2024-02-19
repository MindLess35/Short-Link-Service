package com.shortlink.webapp.repository;

import com.shortlink.webapp.entity.Link;
import com.shortlink.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends
        JpaRepository<User, Long>,
        RevisionRepository<User, Long, Long> {
    Optional<User> findByUsername(String username);

//    @Query(value = """
//            SELECT COUNT(username) = 1
//            FROM User
//            WHERE username = :username
//            """)
//    boolean usernameAlreadyExists(@Param("username") String username);


    boolean existsByUsername(String username);


//    List<Link> findAllUsersLinkById(Long id);

}
