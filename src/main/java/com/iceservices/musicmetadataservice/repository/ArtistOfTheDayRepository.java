package com.iceservices.musicmetadataservice.repository;

import com.iceservices.musicmetadataservice.domain.ArtistOfTheDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArtistOfTheDayRepository extends JpaRepository<ArtistOfTheDay, UUID> {

    Optional<ArtistOfTheDay> findFirstByOrderByEndAtDesc();
}
