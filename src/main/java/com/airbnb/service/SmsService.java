package com.airbnb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SmsService {

    @Value("${fast2sms.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public SmsService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://www.fast2sms.com").build();
    }

    public Mono<String> sendSms(String message, String recipient) {
        String url = "/dev/bulkV2";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authorization", apiKey);

        String requestBody = String.format("{\"route\":\"q\",\"message\":\"%s\",\"language\":\"english\",\"flash\":0,\"numbers\":\"%s\"}", message, recipient);

        return this.webClient.method(HttpMethod.POST)
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(Mono.just(requestBody), String.class)
                .retrieve()
                .bodyToMono(String.class);
    }
}
