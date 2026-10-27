// package com.fitness.aiservice.service;

// import java.util.Map;

// import org.springframework.stereotype.Service;
// import org.springframework.web.reactive.function.client.WebClient;

// import org.springframework.beans.factory.annotation.Value;

// @Service 

// public class GeminiService {

//     private final WebClient webClient;

//     @Value("${gemini.api.url}")
//     private String geminiApiUrl;

//     @Value("${gemini.api.key}")
//     private String geminiApiKey;

//     public GeminiService(WebClient.Builder webClientBuilder){
//         this.webClient = webClientBuilder.build();
//     }

//     public String getAnswer(String question){
//         Map<String, Object> requestBody = Map.of(
//             "contents", new Object[] {
//                 Map.of("parts", new Object[]{
//                     Map.of("text", question)
//                 })
//             }
//         );
//         String response = webClient.post()
//                 .uri(geminiApiUrl + geminiApiKey)
//                 .header("Content-Type", "application/json")
//                 .bodyValue(requestBody)
//                 .retrieve()
//                 .bodyToMono(String.class)
//                 .block();

//                 return response;
//     }
// }

package com.fitness.aiservice.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class GeminiService {

    private final WebClient webClient;

    @Value("${gemini.api.url}")
    private String geminiApiUrl;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${gemini.api.model:gemini-3.6-flash}")
    private String geminiModel;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of(
                "model", geminiModel,
                "input", question
        );

        return webClient.post()
                .uri(geminiApiUrl)
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
