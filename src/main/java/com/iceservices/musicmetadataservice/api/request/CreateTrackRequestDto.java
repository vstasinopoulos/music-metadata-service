package com.iceservices.musicmetadataservice.api.request;

import com.iceservices.musicmetadataservice.domain.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.Duration;
import java.util.UUID;

@Data
public class CreateTrackRequestDto {

    @NotBlank
    private String title;

    @Pattern(regexp = "^other|electronic|classical|jazz|rock|$",message = "Invalid genre")
    private String genre;

    private Duration length;

    @NotNull
    private UUID artistId;
}
