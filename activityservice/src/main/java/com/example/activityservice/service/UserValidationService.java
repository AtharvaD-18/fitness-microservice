package com.example.activityservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j 
@RequiredArgsConstructor 
public class UserValidationService {
    private final WebClient userServicWebClient;

    public boolean validateUser(String userId){
        try{
        return userServicWebClient.get()
                .uri("/api/users/{userId}/validate", userId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
        } catch(WebClientResponseException e){
            if(e.getStatusCode() == org.springframework.http.HttpStatus.NOT_FOUND)
                throw new RuntimeException("User Not Found: " + userId);
            else if(e.getStatusCode() == org.springframework.http.HttpStatus.BAD_REQUEST)
                throw new RuntimeException("Invalid Request: " + userId);
        }
          return false;                        
    }
}
