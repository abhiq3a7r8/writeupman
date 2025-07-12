package com.writeupbackend.writeupman.controller;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.writeupbackend.writeupman.dto.WriteupRequest;
import com.writeupbackend.writeupman.dto.WriteupResponse;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/writeup")
public class WriteupController {
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @PostMapping
    public WriteupResponse generateWriteup(@RequestBody WriteupRequest request) throws JsonProcessingException {
        String subject = request.getSubject();
        String title = request.getTitle();
        String type = request.getType();
        boolean table = request.getTable();

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

        String prompt = switch (type.toLowerCase()) {
            case "aim" ->
                    "Write an aim in one line for the experiment titled \"" + title + "\" in the subject " + subject;
            case "theory" -> "Write a Theory for the experiment in 4 to 5 lines\"" + title + "\" in the subject " + subject;
            case "conclusion" -> "Write a conclusion for the experiment in 4 to 5 lines\"" + title + "\" in the subject " + subject;
            default -> "Write a short description about the experiment \"" + title + "\" in the subject " + subject;
        };


        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        String generatedText = root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text")
                .asText();

        return switch (type.toLowerCase()) {
            case "aim" -> new WriteupResponse(generatedText, null, null);
            case "theory" -> new WriteupResponse(null, generatedText, null);
            case "conclusion" -> new WriteupResponse(null, null, generatedText);
            default -> throw new IllegalArgumentException("Invalid writeup type: " + type);
        };

    }

}
