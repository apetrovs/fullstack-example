package ru.apetrov.piano.backend.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.apetrov.piano.backend.model.SearchResult;
import ru.apetrov.piano.backend.model.SearchResultItem;
import ru.apetrov.piano.backend.model.stackoverflow.Response;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service, that searches question on StackOverflow.
 * Uses StackOverflow api /search.
 *
 * @see <a href="http://api.stackexchange.com/docs/search#order=desc&sort=activity&intitle=java&filter=default&site=stackoverflow&run=true">StackOverflow API</a>
 *
 * @author a.petrov
 */
public class SearchService {

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);
    private static ObjectMapper objectMapper = new ObjectMapper()
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private static final String URL_TEMPLATE = "http://api.stackexchange.com/2.2/search" +
            "?site=stackoverflow" +
            "&order=desc" +
            "&sort=activity" +
            "&intitle=%s" +
            "&page=%s" +
            "&pagesize=%s";

    private final CloseableHttpClient httpClient;

    public SearchService(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Search question on StackOverflow.
     *
     * @param queryText Query text. Can't be null or empty.
     * @param page Page number. Starts from 1.
     * @param pageSize Page size. Can't be less than 1.
     *
     * @return Search result. Contains list of items and flag, that indicates availability of next page.
     */
    public SearchResult queryAnswers(String queryText, Integer page, Integer pageSize) {
        try (CloseableHttpResponse response = httpClient.execute(constructRequest(queryText, page, pageSize))) {
            int responseStatusCode = response.getStatusLine().getStatusCode();
            if (responseStatusCode != HttpStatus.SC_OK) {
                String errorMessage = String.format("StackOverflow returned bad result code %d", responseStatusCode);
                log.error(errorMessage);
                // TODO We need to use custom exceptions for better handling
                throw new RuntimeException(errorMessage);
            } else {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                IOUtils.copy(response.getEntity().getContent(), os);

                Response parsedResponse = objectMapper
                        .readValue(new String(os.toByteArray()), Response.class);

                List<SearchResultItem> items = parsedResponse.getItems().stream()
                        .map(item ->
                                new SearchResultItem(
                                        StringEscapeUtils.unescapeHtml4(item.getTitle()),
                                        item.getOwner().getDisplayName(),
                                        item.getCreationDate(),
                                        item.isAnswered(),
                                        item.getLink()
                                )
                        )
                        .collect(Collectors.toList());

                return new SearchResult(parsedResponse.isHasMore(), items);
            }
        } catch (Exception e) {
            log.error("Error occurred while searching topics. Query text: {}", queryText, e);
            throw new RuntimeException(e);
        }
    }

    private HttpGet constructRequest(String queryText, Integer page, Integer pageSize) throws UnsupportedEncodingException {
        String url = String.format(URL_TEMPLATE, URLEncoder.encode(queryText, "UTF-8"), page, pageSize);

        return new HttpGet(url);
    }
}
