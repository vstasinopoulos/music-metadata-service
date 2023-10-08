package com.iceservices.musicmetadataservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class ArtistNotFoundException extends RuntimeException {
    private final UUID id;
}
