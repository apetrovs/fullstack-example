package ru.apetrov.piano.backend.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.apetrov.piano.backend.model.SearchResult;
import ru.apetrov.piano.backend.service.SearchService;

/**
 * @author a.petrov
 */
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<SearchResult> search(@RequestParam("query") String query,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("pageSize") Integer pageSize) {
        query = StringUtils.trimToNull(query);
        if (query == null) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        if (page == null || page <= 0) {
            return ResponseEntity
                    .badRequest()
                    .build();

        }

        if (pageSize == null || pageSize <= 0) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        try {
            SearchResult searchResult = searchService.queryAnswers(query, page, pageSize);
            return ResponseEntity
                    .ok(searchResult);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
