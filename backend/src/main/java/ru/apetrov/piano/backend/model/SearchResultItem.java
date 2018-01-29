package ru.apetrov.piano.backend.model;

import java.util.Objects;

/**
 * @author a.petrov
 */
public class SearchResultItem {

    private final String title;
    private final String author;
    private final Long creationDate;
    private final boolean answered;
    private final String link;

    public SearchResultItem(String title, String author, Long creationDate, boolean answered, String link) {
        this.title = title;
        this.author = author;
        this.creationDate = creationDate;
        this.answered = answered;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public boolean isAnswered() {
        return answered;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchResultItem)) return false;
        SearchResultItem that = (SearchResultItem) o;
        return answered == that.answered &&
                Objects.equals(title, that.title) &&
                Objects.equals(author, that.author) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, creationDate, answered, link);
    }
}
