package ru.apetrov.piano.backend.model.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author a.petrov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    private final List<ResponseItem> items;
    private final boolean hasMore;

    public Response(@JsonProperty("items") List<ResponseItem> items,
                    @JsonProperty("has_more") boolean hasMore) {
        this.items = items;
        this.hasMore = hasMore;
    }

    public List<ResponseItem> getItems() {
        return items;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}
