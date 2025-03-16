package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService prestamoService;
    
    @PostMapping("/tomar")
    public Prestamo tomarPrestado(@RequestBody Usuario usuario, @RequestBody Libro libro) {
        return prestamoService.tomarPrestado(usuario, libro);
    }
    
    @PutMapping("/devolver/{id}")
    public void devolverPrestamo(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
    }
    
    @PutMapping("/ampliar/{id}")
    public void ampliarPrestamo(@PathVariable Long id) {
        prestamoService.ampliarPrestamo(id);
    }
}
