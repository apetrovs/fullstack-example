package ru.apetrov.piano.backend.rest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.apetrov.piano.backend.model.SearchResult;
import ru.apetrov.piano.backend.service.SearchService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author a.petrov
 */
public class SearchControllerTest {

    private SearchController controller;
    private SearchService service;

    @Before
    public void init() {
        service = mock(SearchService.class);
        controller = new SearchController(service);
    }

    @Test
    public void allArgCorrect_callService() {
        String query = "q";
        Integer page = 1;
        Integer pageSize = 30;

        ResponseEntity<SearchResult> result = controller.search(query, page, pageSize);

        verify(service).queryAnswers(eq(query), eq(page), eq(pageSize));
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void queryWithWhitespaces_callServiceWithTrimmedQuery() {
        String query = "   q   ";
        Integer page = 1;
        Integer pageSize = 30;

        ResponseEntity<SearchResult> result = controller.search(query, page, pageSize);

        verify(service).queryAnswers(eq(query.trim()), eq(page), eq(pageSize));
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void emptyQuery_returnBadRequest() {
        String query = "  ";
        Integer page = 1;
        Integer pageSize = 30;

        ResponseEntity<SearchResult> result = controller.search(query, page, pageSize);

        verify(service, never()).queryAnswers(any(), any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void nonPositivePage_returnBadRequest() {
        String query = "q";
        Integer page = 0;
        Integer pageSize = 30;

        ResponseEntity<SearchResult> result = controller.search(query, page, pageSize);

        verify(service, never()).queryAnswers(any(), any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void nonPositivePageSize_returnBadRequest() {
        String query = "q";
        Integer page = 1;
        Integer pageSize = 0;

        ResponseEntity<SearchResult> result = controller.search(query, page, pageSize);

        verify(service, never()).queryAnswers(any(), any(), any());
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

}