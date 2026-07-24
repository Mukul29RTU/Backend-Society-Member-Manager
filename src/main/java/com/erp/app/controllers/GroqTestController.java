package com.erp.app.controllers;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//import aj.org.objectweb.asm.TypeReference;

@RestController
@RequestMapping("/ai")
public class GroqTestController {

	private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public GroqTestController(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    public Map<String, Object> translateAllFields(Map<String, Object> inputData) {
        try {
//        	Map<String, Object> map = objectMapper.convertValue(inputData, new TypeReference<Map<String, Object>>() {});
            // Convert input map to JSON string for the prompt
            String jsonInput = objectMapper.writeValueAsString(inputData);

            String prompt = """
                Translate the values of the following JSON object into Hindi. 
                Keep the keys exactly the same. 
                Return ONLY the valid JSON object.
                
                Input: %s
                """.formatted(jsonInput);

            // Call Groq
            String aiResponse = chatModel.call(prompt);
            
            // Clean the response (Groq sometimes adds markdown ```json ... ```)
            String cleanedResponse = aiResponse.replaceAll("```json|```", "").trim();

            // Convert back to Map

            return objectMapper.readValue(cleanedResponse, new TypeReference<Map<String, Object>>() {});
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to translate data: " + e.getMessage());
        }
    }
    
    public Map<String, Object> translateEditFields(Map<String, Object> inputData) {
        try {
//        	Map<String, Object> map = objectMapper.convertValue(inputData, new TypeReference<Map<String, Object>>() {});
            // Convert input map to JSON string for the prompt
            String jsonInput = objectMapper.writeValueAsString(inputData);

            String prompt = """
                Translate theses edited values of the following JSON object into Hindi. 
                Keep the keys exactly the same. 
                Return ONLY the valid JSON object and some JSON object has both english and hindi values translate complete into hindi.
                
                Input: %s
                """.formatted(jsonInput);

            // Call Groq
            String aiResponse = chatModel.call(prompt);
  
            // Clean the response (Groq sometimes adds markdown ```json ... ```)
            String cleanedResponse = aiResponse.replaceAll("```json|```", "").trim();

            // Convert back to Map

            return objectMapper.readValue(cleanedResponse, new TypeReference<Map<String, Object>>() {});
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to translate data: " + e.getMessage());
        }
    }
    

    
}
