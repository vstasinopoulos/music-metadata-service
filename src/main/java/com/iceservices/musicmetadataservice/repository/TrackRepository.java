package com.iceservices.musicmetadataservice.repository;

import com.iceservices.musicmetadataservice.domain.Track;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {

    Slice<Track> findAllByArtist_Id(UUID id, Pageable pageable);

}
