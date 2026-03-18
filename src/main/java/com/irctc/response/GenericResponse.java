package com.irctc.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GenericResponse<T> {

    private Boolean success;
    private String message;
    private T data;
    private ErrorResponse errorResponse;
    private T error;


//    Success Response
    public static <T> GenericResponse<T> success(String message, T data){
        return GenericResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

//    Success Response
    public static <T> GenericResponse<T> success(String message){
        return GenericResponse.<T>builder()
                .success(true)
                .message(message)
                .data(null)
                .build();
    }

//    Error Response
    public static <T> GenericResponse<T> error(String message, T data, String errorCode){
        return GenericResponse.<T>builder()
                .success(false)
                .data(null)
                .errorResponse((new ErrorResponse(errorCode)))
                .build();
    }
}
