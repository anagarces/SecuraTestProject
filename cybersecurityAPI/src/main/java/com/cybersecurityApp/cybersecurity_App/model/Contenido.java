package com.cybersecurityApp.cybersecurity_App.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Contenidos")
public class Contenido {

    @Id // clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID (autoincremental)
    @Column(name = "id_contenido") // Mapea este campo a la columna 'id_contenido'
    private Integer idContenido; // Usamos Integer para el ID

    @Column(name = "titulo", nullable = false, length = 255) // Mapea a 'titulo', no puede ser nulo, longitud max 255
    private String titulo;

    @Lob // Indica que es un Large Object, adecuado para tipos TEXT/LONGTEXT
    @Column(name = "cuerpo", nullable = false, columnDefinition = "TEXT") // Mapea a 'cuerpo', no nulo, tipo TEXT
    private String cuerpo;

    @Column(name = "tema", length = 100) // Mapea a 'tema', longitud max 100
    private String tema;

    @Column(name = "nivel_dificultad", length = 50) // Mapea a 'nivel_dificultad', longitud max 50
    private String nivelDificultad;

    // Constructores
    public Contenido() {
        // Constructor vacío requerido por JPA
    }

    public Contenido(String titulo, String cuerpo, String tema, String nivelDificultad) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.tema = tema;
        this.nivelDificultad = nivelDificultad;
    }

    // Getters y Setters
    public Integer getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(Integer idContenido) {
        this.idContenido = idContenido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    /*(Opcional) Metodo toString() para facilitar la depuración */
    @Override
    public String toString() {
        return "Contenido{" +
                "id_contenido=" + idContenido +
                ", titulo='" + titulo + '\'' +
                ", tema='" + tema + '\'' +
                ", nivelDificultad='" + nivelDificultad + '\'' +
                '}';
    }

}


