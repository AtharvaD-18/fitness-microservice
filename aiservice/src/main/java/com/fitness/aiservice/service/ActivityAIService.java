package com.fitness.aiservice.service;

import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Activity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@Slf4j 
@RequiredArgsConstructor 
public class ActivityAIService {
    private final GeminiService geminiservice;

    public String generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiservice.getAnswer(prompt);
        log.info("RESPONSE FROM AI: {}",aiResponse);
        return aiResponse;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
            Analyze this fitnes activity and provide detailed recommendation in the following EXACT JSON format:
            {
                "analysis":{
                    "overall": "Overall analysis here",
                    "pace": "Pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned": "Calories analysis here"
                },
                "improvements":[
                    {
                        "area": "Area name",
                        "recommendation": "Detailed recommendation"
                    }
                ],
                "suggestions": [
                    {
                        "workout": "Workout name",
                        "description": "Detailed workout description"
                    }
                ],
                "safety": [
                    "Safety point 1",
                    "Safety point 2"
                ]
            }

            Analyze this activity:
            Activity Type: %s
            Duration: %d minutes
            Calories Burned: %d
            Additional Metrics: %s

            Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
            Ensure the response follow the EXACT JSON format shown above.
            """,
                    activity.getType(),
                    activity.getDuration(),
                    activity.getCaloriesBurned(),
                    activity.getAdditionalMetrics()
        );
    }
}
