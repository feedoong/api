package io.feedoong.api.controller.response;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResponse<T> {
    List<T> contents;
    int totalPages;
    long totalElements;
    long page;
    long size;

    public PageResponse(Page<T> page) {
        this.contents = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber();
        this.size = page.getSize();
    }
}
