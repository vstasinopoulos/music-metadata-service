package com.iceservices.musicmetadataservice.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreateArtistRequestDto {

    @NotBlank
    private String name;

    private Set<String> nameAliases;
}
