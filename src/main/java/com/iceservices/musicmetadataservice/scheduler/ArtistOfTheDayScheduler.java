package com.iceservices.musicmetadataservice.scheduler;

import com.iceservices.musicmetadataservice.api.response.ArtistResponseDto;
import com.iceservices.musicmetadataservice.service.ArtistService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ArtistOfTheDayScheduler {

    private final ArtistService artistService;

    @Scheduled(cron = "${scheduler.cron}")
    public void checkArtistOfTheDay() {
        log.info("Checking for artist of the day.");
        // TODO Refactor artist service getArtistOfTheDay to split the responsibilities between creation and fetch
        ArtistResponseDto artistOfTheDay = artistService.getArtistOfTheDay();
        log.info("Artist of the day is {}", artistOfTheDay);
    }
}
