package com.biblioteca.controller;

import com.biblioteca.model.Prestamo;
import com.biblioteca.service.PrestamoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prestamos")
@RequiredArgsConstructor
public class PrestamoController {
    private final PrestamoService prestamoService;

    @PostMapping("/tomar")
    public Prestamo tomarPrestado(@RequestBody Map<String, Long> request) {
        Long usuarioId = request.get("usuarioId");
        Long libroId = request.get("libroId");
        return prestamoService.tomarPrestado(usuarioId, libroId);
    }

    @PutMapping("/devolver/{id}")
    public void devolverPrestamo(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
    }

    @PutMapping("/ampliar/{id}")
    public void ampliarPrestamo(@PathVariable Long id) {
        prestamoService.ampliarPrestamo(id);
    }

    @GetMapping("/historial/{usuarioId}")
    public List<Prestamo> obtenerHistorial(@PathVariable Long usuarioId) {
        return prestamoService.obtenerHistorial(usuarioId);
    }

    @GetMapping
    public List<Prestamo> listarPrestamos() {
        return prestamoService.listarTodos();
    }
}


