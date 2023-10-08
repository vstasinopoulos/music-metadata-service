package com.iceservices.musicmetadataservice.converter;

import com.iceservices.musicmetadataservice.api.response.ArtistResponseDto;
import com.iceservices.musicmetadataservice.domain.Artist;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ArtistToArtistResponseDtoConverter implements Converter<Artist, ArtistResponseDto> {

    @Override
    public ArtistResponseDto convert(Artist source) {
        return ArtistResponseDto.builder()
                .name(source.getName())
                .nameAliases(source.getNameAliases())
                .created(source.getCreated().atZone(ZoneId.systemDefault()))
                .modified(source.getModified().atZone(ZoneId.systemDefault()))
                .build();
    }
}
