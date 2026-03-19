package com.irctc.exception;

import com.irctc.response.ErrorResponse;
import com.irctc.response.GenericResponse;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
@ToString
public class GlobalException {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<Object>> methodArgumentException(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        Map<String, Object> errorResponse = new HashMap<>();

        allErrors.forEach(error ->{
            String message = error.getDefaultMessage();
            String field = ((FieldError) error).getField();

            errorResponse.put(field, message);
        });

//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        return ResponseEntity.badRequest()
                .body(
                        GenericResponse.builder()
                                .success(false)
//                                .message("Validation Failed")
//                                .errorResponse(new ErrorResponse("Validation Error"))
                                .data(null)
                                .message(errorResponse.toString())
//                                .error(errorResponse)
                                .build()
                );
    }

    @ExceptionHandler(DuplicateFieldException.class)
    public ResponseEntity<GenericResponse<Object>> handleDuplicateFieldException(DuplicateFieldException ex){
        String message = ex.getMessage();
//        return new ResponseEntity<>(HttpStatus.FOUND, GenericResponse.builder()
//                .success(false)
//                .message(message)
//                .data(null)
//                .build()
//        );
        return ResponseEntity.status(HttpStatus.FOUND).body(
                GenericResponse.builder()
                        .success(false)
                        .message(message)
                        .data(null)
                        .build()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GenericResponse.builder()
                        .success(false)
                        .message(ex.getMessage())
                        .data(null)
                        .build());
    }

}
