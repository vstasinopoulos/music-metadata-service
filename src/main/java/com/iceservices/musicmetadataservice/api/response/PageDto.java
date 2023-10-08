package com.iceservices.musicmetadataservice.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class PageDto {

    private int pageSize;

    private int pageNumber;
}
