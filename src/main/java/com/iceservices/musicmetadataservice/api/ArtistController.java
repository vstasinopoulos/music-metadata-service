package com.iceservices.musicmetadataservice.api;

import com.iceservices.musicmetadataservice.api.request.AddArtistNameAliasesRequestDto;
import com.iceservices.musicmetadataservice.api.request.CreateArtistRequestDto;
import com.iceservices.musicmetadataservice.api.response.ArtistResponseDto;
import com.iceservices.musicmetadataservice.service.ArtistService;
import jakarta.validation.Valid;
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
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping(path = "v1/artists", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createArtist(@Valid @RequestBody CreateArtistRequestDto request) {
        UUID id = artistService.createArtist(request.getName(), request.getNameAliases());
        return ResponseEntity.created(URI.create(String.format("/artists/%s", id.toString()))).build();
    }

    @PatchMapping(path = "v1/artists/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addArtistNameAliases(@Valid @RequestBody AddArtistNameAliasesRequestDto request,
                                                     @PathVariable UUID id) {
        artistService.addArtistNameAliases(id, request.getNameAliases());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "v1/artists/artist-of-the-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArtistResponseDto> getArtistOfTheDay() {
        return ResponseEntity.ok(artistService.getArtistOfTheDay());
    }

}
