package com.iceservices.musicmetadataservice.api.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageableResponseDto<T> {

    private List<T> data;

    private PageDto nextPage;

    private boolean hasMore;

}
