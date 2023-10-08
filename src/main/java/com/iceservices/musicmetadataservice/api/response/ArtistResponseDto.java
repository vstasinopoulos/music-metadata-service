package com.iceservices.musicmetadataservice.api.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
public class ArtistResponseDto {

    private String name;

    private Set<String> nameAliases;

    private ZonedDateTime created;

    private ZonedDateTime modified;
}
