package ru.apetrov.piano.backend.model.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author a.petrov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseItem {

    private final String title;
    private final boolean answered;
    private final ItemOwner owner;
    private final Long creationDate;
    private final String link;

    public ResponseItem(@JsonProperty("title") String title,
                        @JsonProperty("is_answered") boolean answered,
                        @JsonProperty("owner") ItemOwner owner,
                        @JsonProperty("creation_date") Long creationDate,
                        @JsonProperty("link") String link) {
        this.title = title;
        this.answered = answered;
        this.owner = owner;
        this.creationDate = creationDate;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAnswered() {
        return answered;
    }

    public ItemOwner getOwner() {
        return owner;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public String getLink() {
        return link;
    }
}
