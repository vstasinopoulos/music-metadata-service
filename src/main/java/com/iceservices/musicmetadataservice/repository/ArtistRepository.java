package com.iceservices.musicmetadataservice.repository;

import com.iceservices.musicmetadataservice.domain.Artist;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    Optional<Artist> findFirstByOrderByCreatedAsc(OffsetScrollPosition position);
}
