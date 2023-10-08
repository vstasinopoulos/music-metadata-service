package com.iceservices.musicmetadataservice.api.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class AddArtistNameAliasesRequestDto {

    @NotEmpty
    private Set<String> nameAliases;

}
