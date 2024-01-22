package com.example.api.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiErrorResponse {

    private int status;

    private String message;

    private Date timestamp;

    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Map<String, String>> validations;

    public ApiErrorResponse(){}

    public ApiErrorResponse(int status, String message, Date timestamp, String description, boolean validations) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.description = description;
        this.validations = validations ? mapValidations(message) : null;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public List<Map<String, String>> getValidations() {
        return validations;
    }

    private List<Map<String, String>> mapValidations(String errorMessage) {

        if (errorMessage.startsWith("Validation failed for classes")) {
            errorMessage = convertErrorMessage(errorMessage);
            this.message = errorMessage;
        }

        List<Map<String, String>> violationsList = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\w+): (.+?)(?=, \\w+:|$)");
        Matcher matcher = pattern.matcher(errorMessage);

        while (matcher.find()) {
            Map<String, String> errorMap = new HashMap<>();
            String key = matcher.group(1);
            String value = matcher.group(2);
            errorMap.put(key, value.trim());
            violationsList.add(errorMap);
        }
        return violationsList;
    }

    private String convertErrorMessage(String errorMessage) {
        List<String> convertedMessages = new ArrayList<>();

        Pattern classPattern = Pattern.compile("Validation failed for classes \\[(.*?)\\]");
        Matcher classMatcher = classPattern.matcher(errorMessage);

        Pattern violationPattern =
                Pattern.compile("ConstraintViolationImpl\\{interpolatedMessage='(.*?)', propertyPath=(.*?), .*?}");
        Matcher violationMatcher = violationPattern.matcher(errorMessage);

        if (classMatcher.find()) {
            String className = classMatcher.group(1);

            while (violationMatcher.find()) {
                String message = violationMatcher.group(1);
                String propertyPath = violationMatcher.group(2);

                String formattedMessage = String.format("%s: %s", propertyPath, message);
                convertedMessages.add(formattedMessage);
            }
        }

        return String.join(", ", convertedMessages);
    }
}
