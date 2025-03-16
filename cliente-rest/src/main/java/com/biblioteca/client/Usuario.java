package com.biblioteca.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    private Long id;
    private String nombre;
    private String matricula;
    private LocalDate fechaNacimiento;
    private String correo;
    private LocalDate fechaSancion;
}
