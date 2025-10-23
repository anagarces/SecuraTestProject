package com.cybersecurityApp.cybersecurity_App.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

//Esta clase captura y maneja las excepciones globalmente
//Especialmente las de validacion que ocurren cuando @Valid detecta erres en los DTOs
//ej, email mal formado, contrasena corta, etc..
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){

            Map<String, String> errors = new HashMap<>();

            //Por cada error en el DTO, guardamos el nombre del campo y el mensaje

    for(FieldError error : ex.getBindingResult().getFieldErrors()){

        errors.put(error.getField(), error.getDefaultMessage());
    }

        //Devuelve HTTP 400 con el cuerpo {campo: mensaje}
        return ResponseEntity.badRequest().body(errors);
    }

}
