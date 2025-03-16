package com.biblioteca.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    private Long id;
    private String titulo;
    private String autor;
    private String edicion;
    private String ISBN;
    private String editorial;
    private boolean disponible;
}
