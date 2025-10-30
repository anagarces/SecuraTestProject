package com.cybersecurityApp.cybersecurity_App.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") //especifica el nombre de la tabla en la bbbdd
public class Usuario {



    @Id //marcar este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false) //indica que este campo debe ser obligatorio
    private String password;

    private String nombre; //campo opcional puede ser nulo

//constructor vacio obligatorio para que el motor de persistencia JPA pueda crear objetos de esta clase
    public Usuario() {}

    public Usuario(Long id) {
        this.id = id;
    }
    //getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
