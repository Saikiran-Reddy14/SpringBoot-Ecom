package com.ecommerce.spring.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "message", "status", "timestamp", "data" })
public class ApiResponse<T> {

    private String message;
    private int status;
    private T data;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(String message, int status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
}
