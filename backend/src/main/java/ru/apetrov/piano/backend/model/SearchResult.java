package ru.apetrov.piano.backend.model;

import java.util.List;

/**
 * @author a.petrov
 */
public class SearchResult {

    private final boolean hasMore;
    private final List<SearchResultItem> items;

    public SearchResult(boolean hasMore, List<SearchResultItem> items) {
        this.hasMore = hasMore;
        this.items = items;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public List<SearchResultItem> getItems() {
        return items;
    }
}
