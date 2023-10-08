package com.iceservices.musicmetadataservice.converter;

import com.iceservices.musicmetadataservice.api.response.TrackResponseDto;
import com.iceservices.musicmetadataservice.domain.Track;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class TrackToTrackResponseDtoConverter implements Converter<Track, TrackResponseDto> {

    @Override
    public TrackResponseDto convert(Track source) {
        return TrackResponseDto.builder()
                .genre(source.getGenre().toString().toLowerCase())
                .title(source.getTitle())
                .length(source.getLength())
                .created(source.getCreated().atZone(ZoneId.systemDefault()))
                .modified(source.getModified().atZone(ZoneId.systemDefault()))
                .build();
    }
}
