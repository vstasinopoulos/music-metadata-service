package com.iceservices.musicmetadataservice.service;

import com.iceservices.musicmetadataservice.api.request.CreateTrackRequestDto;
import com.iceservices.musicmetadataservice.api.response.PageDto;
import com.iceservices.musicmetadataservice.api.response.PageableResponseDto;
import com.iceservices.musicmetadataservice.api.response.TrackResponseDto;
import com.iceservices.musicmetadataservice.domain.Artist;
import com.iceservices.musicmetadataservice.domain.Genre;
import com.iceservices.musicmetadataservice.domain.Track;
import com.iceservices.musicmetadataservice.exception.ArtistNotFoundException;
import com.iceservices.musicmetadataservice.repository.ArtistRepository;
import com.iceservices.musicmetadataservice.repository.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackService {

    private final ArtistRepository artistRepository;
    private final TrackRepository trackRepository;

    private final ConversionService conversionService;

    public UUID createTrack(CreateTrackRequestDto request) {
        try {
            Artist artist = artistRepository.getReferenceById(request.getArtistId());

            Track track = Track.builder()
                    .title(request.getTitle())
                    .artist(artist)
                    .genre(Optional.ofNullable(request.getGenre())
                            .map(g -> Genre.valueOf(g.toUpperCase()))
                            .orElse(null))
                    .length(request.getLength())
                    .build();
            return trackRepository.save(track).getId();
        } catch (EntityNotFoundException | DataIntegrityViolationException e) {
            log.warn("Artist with id {} not found.", request.getArtistId());
            throw new ArtistNotFoundException(request.getArtistId());
        }
    }

    public PageableResponseDto<TrackResponseDto> getArtistTracks(UUID id, int pageNumber, int pageSize) {
        // TODO Consider keyset scroll paging for performance
        Slice<Track> trackSlice = trackRepository.findAllByArtist_Id(id, PageRequest.of(pageNumber, pageSize, Sort.by("created").ascending()));

        List<TrackResponseDto> tracks = trackSlice.getContent().stream().map(track -> conversionService.convert(track, TrackResponseDto.class)).toList();

        if (!trackSlice.hasNext()) {
            return PageableResponseDto.<TrackResponseDto>builder()
                    .data(tracks)
                    .hasMore(false)
                    .build();
        }

        return PageableResponseDto.<TrackResponseDto>builder()
                .data(tracks)
                .hasMore(true)
                .nextPage(PageDto.of(trackSlice.nextPageable().getPageSize(), trackSlice.nextPageable().getPageNumber()))
                .build();
    }
}
