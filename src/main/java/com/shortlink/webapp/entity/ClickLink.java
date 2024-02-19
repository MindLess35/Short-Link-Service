package com.shortlink.webapp.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private LocalDateTime usageTime;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Link link;
}
