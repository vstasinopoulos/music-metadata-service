package com.iceservices.musicmetadataservice.api;

import com.iceservices.musicmetadataservice.api.request.CreateTrackRequestDto;
import com.iceservices.musicmetadataservice.api.response.PageableResponseDto;
import com.iceservices.musicmetadataservice.api.response.TrackResponseDto;
import com.iceservices.musicmetadataservice.service.TrackService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class TrackController {

    private final TrackService trackService;

    @PostMapping(path = "v1/tracks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createTrack(@Valid @RequestBody CreateTrackRequestDto request) {
        UUID id = trackService.createTrack(request);
        return ResponseEntity.created(URI.create(String.format("/tracks/%s", id.toString()))).build();
    }

    @GetMapping(path = "v1/tracks/{artistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PageableResponseDto<TrackResponseDto>> getArtistTracks(@PathVariable UUID artistId,
                                                                                 @RequestParam(defaultValue = "0") @Min(0) int pageNumber,
                                                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(100) int pageSize) {
        return ResponseEntity.ok(trackService.getArtistTracks(artistId, pageNumber, pageSize));
    }
}
