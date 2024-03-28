package com.shortlink.webapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "click_links")
public class ClickLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant usageTime;

    //    @JoinColumn(nullable = true)
//    @JoinColumn(name = "link_id")//, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY//, cascade = CascadeType.ALL
//            , optional = false
    )
    private Link link;
}
