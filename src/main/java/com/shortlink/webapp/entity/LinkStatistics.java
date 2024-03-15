package com.shortlink.webapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "link_statistics")
@EntityListeners(AuditingEntityListener.class)
public class LinkStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @JoinColumn(name = "link_id")
//    @JoinColumn(name = "link_id", nullable = false , insertable = false, updatable = false)
//    @JoinColumn(name = "link_id", nullable = false, insertable = true, updatable = true)
    @OneToOne(fetch = FetchType.LAZY)
    private Link link;

    @CreatedDate
    @Column(name = "date_of_creation", nullable = false, updatable = false)
    private LocalDateTime dateOfCreation;

    @LastModifiedDate
    @Column(name = "date_of_last_uses", insertable = false)
    private LocalDateTime dateOfLastUses;

    @Builder.Default
    @Column(name = "count_of_uses", nullable = false)
    private Long countOfUses = 0L;

    @Column(name = "life_time", nullable = false)
    private Long lifeTime;

}