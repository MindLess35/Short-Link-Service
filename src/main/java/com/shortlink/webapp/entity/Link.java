package com.shortlink.webapp.entity;

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

    @Column(name = "key")
    private String key;

    //    @JoinColumn
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(
            mappedBy = "link",
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH

            }
    )
//    @Column(nullable = true)
    private List<ClickLink> clickLink;

    @OneToOne(
            mappedBy = "link",
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {
//                    CascadeType.ALL
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
                    //TODO
            }
    )
    private LinkStatistics linkStatistics;

}