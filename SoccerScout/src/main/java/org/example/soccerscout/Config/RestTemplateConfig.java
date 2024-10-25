package org.example.soccerscout.Config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
public class RestTemplateConfig {

    // List of proxies
    private final List<String> proxies = Arrays.asList(
            "113.160.133.85:8080",  // Vietnam
            "43.134.32.184:3128",   // Singapore
            "189.240.60.168:9090",  // Mexico
            "20.111.54.16:8123",    // France
            "143.110.232.177:80"     // US
    );

    @Bean
    public RestTemplate restTemplate() {
        // Randomly select a proxy
        String selectedProxy = getRandomProxy();

        String[] proxyParts = selectedProxy.split(":");
        String host = proxyParts[0];
        int port = Integer.parseInt(proxyParts[1]);

        // Create a connection manager
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(10); // Set max connections per route
        cm.setMaxTotal(100); // Set max total connections

        // Build the HTTP client with the selected proxy
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setProxy(new HttpHost(host, port)) // Set the selected proxy here
                .build();

        // Create a request factory using the new HttpClient
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        // Return the RestTemplate instance
        return new RestTemplate(requestFactory);
    }

    // Method to get a random proxy from the list
    private String getRandomProxy() {
        Random random = new Random();
        return proxies.get(random.nextInt(proxies.size()));
    }
}
