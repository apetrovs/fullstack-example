package ru.apetrov.piano.backend.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.apetrov.piano.backend.rest.SearchController;
import ru.apetrov.piano.backend.service.SearchService;

/**
 * @author a.petrov
 */
@Configuration
public class BackendConfig {

    @Bean
    public SearchService searchService(CloseableHttpClient httpClient) {
        return new SearchService(httpClient);
    }

    @Bean
    public SearchController queryRest(SearchService searchService) {
        return new SearchController(searchService);
    }

    @Bean(destroyMethod = "close")
    public CloseableHttpClient closeableHttpClient() {
        return HttpClientBuilder
                .create()
                .build();
    }
}
