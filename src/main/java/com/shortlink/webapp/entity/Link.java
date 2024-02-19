package com.shortlink.webapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_link", nullable = false)
    private String originalLink;

    @Column(nullable = false, unique = true)
    private String shortLink;

    @Column(name = "encrypted_key")
    private String encryptedKey;

    @JsonIgnore
    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JsonIgnore
    @OneToMany(
            mappedBy = "link",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private List<ClickLink> clickLink;

    @JsonIgnore
    @OneToOne(
            mappedBy = "link",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    private LinkStatistics linkStatistics;

}