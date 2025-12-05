package com.cybersecurityApp.cybersecurity_App.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {

    // Regex para nombre: solo letras, espacios, acentos y ñ
    @NotBlank(message = "El nombre es obligatorio.")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "El nombre solo puede contener letras y espacios.")
    private String nombre;

    @NotBlank(message = "El email es obligatorio.")
    @Email(message = "El formato del email es inválido.")
    private String email;

    // Aumentar la longitud mínima a 8.
    // Aunque la validación completa (Mayúsculas, símbolos) a menudo se maneja en el servicio/config de Spring Security,
    // se puede añadir un Pattern aquí para validación temprana si no se usa Spring Security por defecto.
    // Usaremos una versión simplificada de la regex robusta del frontend.
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe contener Mayúsculas, Minúsculas, números y símbolos.")
    private String password;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}