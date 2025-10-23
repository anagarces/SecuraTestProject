package com.cybersecurityApp.cybersecurity_App.model.dto;

//funcion de esta clase es llevar un token de seguridad desde el servidor al cliente
//este token sera utilizado para probar su identidad en las siguientes peticiones
public class AuthResponseDTO {

    private String token;

    public AuthResponseDTO(String token) {
        this.token = token;
    }
    // Getter y Setter
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
