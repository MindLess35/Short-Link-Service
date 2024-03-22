package com.shortlink.webapp.repository;

import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.repository.custom.user.FilteringPaginationUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends
        JpaRepository<User, Long>,
        FilteringPaginationUserRepository,
        RevisionRepository<User, Long, Long> {
    Optional<User> findByUsername(String username);

//    @Query(value = """
//            SELECT COUNT(username) = 1
//            FROM User
//            WHERE username = :username
//            """)
//    boolean usernameAlreadyExists(@Param("username") String username);


    boolean existsByUsername(String username);
//    boolean existsByUsername(String username);


    boolean existsById(Long id);

    boolean existsByEmail(String email);

    @Modifying
    @Query("""
            UPDATE User u
            SET u.verified = true
            WHERE u.id = :userId
            """)
    void updateVerifiedById(Long userId);

    @Modifying
    @Query("""
            UPDATE User u
            SET u.password = :newPassword
            WHERE u.id = :userId
            """)
    void updatePasswordById(String newPassword, Long userId);

    @Query("""
           SELECT u
           FROM User u
           WHERE u.email = :emailOrUsername OR u.username = :emailOrUsername
           """)
    Optional<User> findByEmailOrUsername(String emailOrUsername);

//    List<Link> findAllUsersLinkById(Long id);

}
