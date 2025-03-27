package com.cosmosodyssey.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.cosmosodyssey.DTOs.PriceListDto;

@Service
public class PriceListFetchService {

    private final WebClient webClient;

    public PriceListFetchService(WebClient.Builder webClientBuilder,
                                 @Value("${cosmos.odyssey.api.url}") String apiUrl) {
        this.webClient = webClientBuilder.baseUrl(apiUrl).build();
    }

    public Mono<PriceListDto> fetchPriceList() {
        return webClient.get()
                .retrieve()
                .bodyToMono(PriceListDto.class);
    }
}
