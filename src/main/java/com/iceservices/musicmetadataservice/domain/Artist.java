package com.iceservices.musicmetadataservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "artist")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "name_aliases")
    private Set<String> nameAliases;

    @Column
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private Set<Track> tracks;

    @CreationTimestamp
    @Column(nullable = false)
    private Instant created;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant modified;

    @Version
    @Column(nullable = false)
    private Long version;
}
