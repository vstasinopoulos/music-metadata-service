package com.iceservices.musicmetadataservice.service;

import com.iceservices.musicmetadataservice.api.response.ArtistResponseDto;
import com.iceservices.musicmetadataservice.domain.Artist;
import com.iceservices.musicmetadataservice.domain.ArtistOfTheDay;
import com.iceservices.musicmetadataservice.exception.ArtistNotFoundException;
import com.iceservices.musicmetadataservice.exception.ArtistOfTheDayNotFoundException;
import com.iceservices.musicmetadataservice.repository.ArtistOfTheDayRepository;
import com.iceservices.musicmetadataservice.repository.ArtistRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistOfTheDayRepository artistOfTheDayRepository;

    private final ConversionService conversionService;

    public UUID createArtist(String name, Set<String> nameAliases) {
        Artist artist = Artist.builder().name(name).nameAliases(nameAliases).build();
        return artistRepository.save(artist).getId();
    }

    public void addArtistNameAliases(UUID id, Set<String> nameAliases) {
        try {
            Artist artist = artistRepository.getReferenceById(id);
            if (artist.getNameAliases() == null) {
                artist.setNameAliases(nameAliases);
            } else {
                artist.setNameAliases(Stream.concat(artist.getNameAliases().stream(), nameAliases.stream()).collect(Collectors.toSet()));
            }
            artistRepository.save(artist);
        } catch (EntityNotFoundException e) {
            log.warn("Artist with id {} not found.", id);
            throw new ArtistNotFoundException(id);
        }
    }

    // TODO Distributed locking
    @Transactional
    public ArtistResponseDto getArtistOfTheDay() {
        Optional<ArtistOfTheDay> currentArtistOfTheDayWithOptional = artistOfTheDayRepository.findFirstByOrderByEndAtDesc();

        if (currentArtistOfTheDayWithOptional.isEmpty()) {
            Optional<Artist> artist = artistRepository.findFirstByOrderByCreatedAsc(ScrollPosition.offset());

            if (artist.isPresent()) {
                createAndSaveArtistOfTheDay(artist.get(), 0L);
                return conversionService.convert(artist.get(), ArtistResponseDto.class);
            }
            throw new ArtistOfTheDayNotFoundException();
        }

        ArtistOfTheDay currentArtistOfTheDay = currentArtistOfTheDayWithOptional.get();

        if (Instant.now().isAfter(currentArtistOfTheDay.getEndAt())) {
            long offset = currentArtistOfTheDay.getSeq() + 1L;

            Optional<Artist> artist = artistRepository.findFirstByOrderByCreatedAsc(ScrollPosition.offset(offset));

            if (artist.isPresent()) {
                createAndSaveArtistOfTheDay(artist.get(), offset);
                return conversionService.convert(artist.get(), ArtistResponseDto.class);
            }

            artist = artistRepository.findFirstByOrderByCreatedAsc(ScrollPosition.offset(0));

            createAndSaveArtistOfTheDay(artist.get(), 0); // Loop artists from the first one
            return conversionService.convert(artist.get(), ArtistResponseDto.class);
        }

        return conversionService.convert(currentArtistOfTheDay.getArtist(), ArtistResponseDto.class);
    }

    private void createAndSaveArtistOfTheDay(Artist artist, long offset) {
        ZonedDateTime startOfTheDay = ZonedDateTime.now(ZoneId.of("UTC")).withHour(0).withMinute(0).withSecond(0).withNano(0);

        ArtistOfTheDay artistOfTheDay = ArtistOfTheDay.builder().artist(artist).seq(offset).startAt(startOfTheDay.toInstant()).endAt(startOfTheDay.plusDays(1).toInstant()).build();

        artistOfTheDayRepository.save(artistOfTheDay);
    }
}
