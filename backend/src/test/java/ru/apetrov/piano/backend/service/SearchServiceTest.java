package ru.apetrov.piano.backend.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Before;
import org.junit.Test;
import ru.apetrov.piano.backend.model.SearchResult;
import ru.apetrov.piano.backend.model.SearchResultItem;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author a.petrov
 */
public class SearchServiceTest {

    private static final String CORRECT_RESPONSE = "{\n" +
            "  \"items\": [\n" +
            "    {\n" +
            "      \"tags\": [\n" +
            "        \"java\",\n" +
            "        \"attributes\",\n" +
            "        \"single-sign-on\",\n" +
            "        \"saml\",\n" +
            "        \"assertion\"\n" +
            "      ],\n" +
            "      \"owner\": {\n" +
            "        \"reputation\": 1,\n" +
            "        \"user_id\": 9219002,\n" +
            "        \"user_type\": \"registered\",\n" +
            "        \"profile_image\": \"https://www.gravatar.com/avatar/92c30f64972f0e1afa748aa981d233ca?s=128&d=identicon&r=PG&f=1\",\n" +
            "        \"display_name\": \"Mohamed\",\n" +
            "        \"link\": \"https://stackoverflow.com/users/9219002/mohamed\"\n" +
            "      },\n" +
            "      \"is_answered\": true,\n" +
            "      \"view_count\": 10,\n" +
            "      \"answer_count\": 0,\n" +
            "      \"score\": 0,\n" +
            "      \"last_activity_date\": 1517229734,\n" +
            "      \"creation_date\": 1517225713,\n" +
            "      \"last_edit_date\": 1517229734,\n" +
            "      \"question_id\": 48500327,\n" +
            "      \"link\": \"https://stackoverflow.com/questions/48500327/java-how-to-get-saml-assertion-attributes\",\n" +
            "      \"title\": \"Java - How to get SAML Assertion Attributes\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"tags\": [\n" +
            "        \"java\",\n" +
            "        \"regex\"\n" +
            "      ],\n" +
            "      \"owner\": {\n" +
            "        \"reputation\": 849,\n" +
            "        \"user_id\": 666176,\n" +
            "        \"user_type\": \"registered\",\n" +
            "        \"accept_rate\": 75,\n" +
            "        \"profile_image\": \"https://i.stack.imgur.com/mHOjo.png?s=128&g=1\",\n" +
            "        \"display_name\": \"Cortlendt\",\n" +
            "        \"link\": \"https://stackoverflow.com/users/666176/cortlendt\"\n" +
            "      },\n" +
            "      \"is_answered\": false,\n" +
            "      \"view_count\": 55,\n" +
            "      \"answer_count\": 3,\n" +
            "      \"score\": 2,\n" +
            "      \"last_activity_date\": 1517229582,\n" +
            "      \"creation_date\": 1517224245,\n" +
            "      \"last_edit_date\": 1517229582,\n" +
            "      \"question_id\": 48499879,\n" +
            "      \"link\": \"https://stackoverflow.com/questions/48499879/updated-java-regex-match-cors-urls-with-wildcards\",\n" +
            "      \"title\": \"(Updated) JAVA, REGEX - match CORS urls with wildcards\"\n" +
            "    }" +
            "  ]," +
            "  \"has_more\": false" +
            "}";

    private static ObjectMapper objectMapper = new ObjectMapper()
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

    private CloseableHttpClient httpClient;
    private SearchService service;

    @Before
    public void init() {
        httpClient = mock(CloseableHttpClient.class);
        service = new SearchService(httpClient);
    }

    @Test
    public void correctResponse_returnParsedResult() throws IOException {
        StatusLine statusLineMock = mock(StatusLine.class);
        when(statusLineMock.getStatusCode()).thenReturn(HttpStatus.SC_OK);

        HttpEntity entityMock = mock(HttpEntity.class);
        when(entityMock.getContent()).thenReturn(new ByteArrayInputStream(CORRECT_RESPONSE.getBytes()));

        CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);
        when(responseMock.getEntity()).thenReturn(entityMock);

        when(httpClient.execute(any())).thenReturn(responseMock);

        SearchResult result = service.queryAnswers("q", 1, 30);
        verify(httpClient).execute(any());

        assertEquals(false, result.isHasMore());
        assertNotNull(result.getItems());
        assertEquals(2, result.getItems().size());

        assertTrue(result.getItems().contains(
                new SearchResultItem(
                        "Java - How to get SAML Assertion Attributes",
                        "Mohamed",
                        1517225713L,
                        true,
                        "https://stackoverflow.com/questions/48500327/java-how-to-get-saml-assertion-attributes"
                )
        ));

        assertTrue(result.getItems().contains(
                new SearchResultItem(
                        "(Updated) JAVA, REGEX - match CORS urls with wildcards",
                        "Cortlendt",
                        1517224245L,
                        false,
                        "https://stackoverflow.com/questions/48499879/updated-java-regex-match-cors-urls-with-wildcards"
                )
        ));
    }

    @Test(expected = RuntimeException.class)
    public void badResultResponse_throwException() throws IOException {
        StatusLine statusLineMock = mock(StatusLine.class);
        when(statusLineMock.getStatusCode()).thenReturn(HttpStatus.SC_INTERNAL_SERVER_ERROR);

        CloseableHttpResponse responseMock = mock(CloseableHttpResponse.class);
        when(responseMock.getStatusLine()).thenReturn(statusLineMock);

        when(httpClient.execute(any())).thenReturn(responseMock);

        service.queryAnswers("q", 1, 30);
    }
}