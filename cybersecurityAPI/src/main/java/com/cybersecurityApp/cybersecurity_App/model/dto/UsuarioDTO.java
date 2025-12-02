package com.cybersecurityApp.cybersecurity_App.model.dto;

public class UsuarioDTO {

    private Long id; // CAMBIO: De Integer a Long
    private String email;
    private String nombre;
    private String role;     // Lo dejamos como String, pero lo convertiremos en el controller
    private boolean active;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}