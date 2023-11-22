package io.feedoong.api.controller.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {
    private final List<T> contents;
    private final int totalPages;
    private final long totalElements;
    private final long page;
    private final long size;
    private final Boolean hasNext;
    private final Boolean isFirst;
    private final Boolean isLast;

    public PageResponse(Page<T> page) {
        this.contents = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.hasNext = page.hasNext();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }
}
