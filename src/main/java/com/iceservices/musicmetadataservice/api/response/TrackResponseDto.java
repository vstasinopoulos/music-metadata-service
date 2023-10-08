package com.iceservices.musicmetadataservice.api.response;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

@Data
@Builder
public class TrackResponseDto {

    private String title;

    private String genre;

    private Duration length;

    private ZonedDateTime created;

    private ZonedDateTime modified;

}
