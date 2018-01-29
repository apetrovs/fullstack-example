package ru.apetrov.piano.backend.model.stackoverflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author a.petrov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemOwner {

    private final String displayName;

    public ItemOwner(@JsonProperty("display_name") String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
