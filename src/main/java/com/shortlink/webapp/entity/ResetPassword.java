package com.shortlink.webapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime resetAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

}
