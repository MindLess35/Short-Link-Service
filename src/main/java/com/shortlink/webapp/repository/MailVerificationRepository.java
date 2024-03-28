package com.shortlink.webapp.repository;

import com.shortlink.webapp.dto.projection.MailVerificationWithUserProjection;
import com.shortlink.webapp.entity.MailVerification;
import com.shortlink.webapp.entity.User;
import com.shortlink.webapp.repository.custom.user.FilteringPaginationUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MailVerificationRepository extends JpaRepository<MailVerification, Long> {

    @Query("""
            SELECT u.id AS userId,
                   u.verified AS verified,
                   mv.id AS verificationId,
                   mv.createdAt AS createdAt
            FROM MailVerification mv
            JOIN mv.user u
            WHERE mv.token = :token
            """)
    Optional<MailVerificationWithUserProjection> findVerificationWithUserByToken(String token);

    @Modifying
    @Query("""
            UPDATE MailVerification mv
            SET mv.verifiedAt = :verifiedAt
            WHERE mv.id = :verificationId
            """)
    void updateVerifiedAtById(Long verificationId, Instant verifiedAt);
}
