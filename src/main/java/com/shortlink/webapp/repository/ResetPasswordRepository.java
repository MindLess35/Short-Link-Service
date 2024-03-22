package com.shortlink.webapp.repository;

import com.shortlink.webapp.dto.projection.MailVerificationWithUserProjection;
import com.shortlink.webapp.dto.projection.ResetPasswordWithUserProjection;
import com.shortlink.webapp.entity.MailVerification;
import com.shortlink.webapp.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    @Query("""
           SELECT u.id AS userId,
                  rp.createdAt AS createdAt,
                  rp.resetAt AS resetAt,
                  rp.id AS resetId
           FROM ResetPassword rp
           JOIN rp.user u
           WHERE rp.token = :token
           """)
    Optional<ResetPasswordWithUserProjection> findByToken(String token);

    @Modifying
    @Query("""
            UPDATE ResetPassword rp
            SET rp.resetAt = :resetAt
            WHERE rp.id = :resetId
            """)
    void updateResetAtById(LocalDateTime resetAt, Long resetId);
//    @Query("""
//            SELECT u.id AS userId,
//                   u.verified AS verified,
//                   mv.id AS verificationId,
//                   mv.createdAt AS createdAt
//            FROM MailVerification mv
//            JOIN mv.user u
//            WHERE mv.token = :token
//            """)
//    Optional<MailVerificationWithUserProjection> findVerificationWithUserByToken(String token);
//
//    @Modifying
//    @Query("""
//            UPDATE MailVerification mv
//            SET mv.verifiedAt = :verifiedAt
//            WHERE mv.id = :verificationId
//            """)
//    void updateVerifiedAtById(Long verificationId, LocalDateTime verifiedAt);
}
