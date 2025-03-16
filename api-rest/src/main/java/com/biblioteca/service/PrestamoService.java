package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    
    public Prestamo tomarPrestado(Usuario usuario, Libro libro) {
        if (!libro.isDisponible()) {
            throw new RuntimeException("El libro no está disponible");
        }
        if (usuario.getFechaSancion() != null && usuario.getFechaSancion().isAfter(LocalDate.now())) {
            throw new RuntimeException("El usuario está sancionado hasta " + usuario.getFechaSancion());
        }
        libro.setDisponible(false);
        libroRepository.save(libro);
        Prestamo prestamo = new Prestamo(null, usuario, libro, LocalDate.now(), LocalDate.now().plusWeeks(2), false);
        return prestamoRepository.save(prestamo);
    }
    
    public void devolverPrestamo(Long prestamoId) {
        Optional<Prestamo> optionalPrestamo = prestamoRepository.findById(prestamoId);
        if (optionalPrestamo.isPresent()) {
            Prestamo prestamo = optionalPrestamo.get();
            prestamo.setDevuelto(true);
            prestamo.getLibro().setDisponible(true);
            libroRepository.save(prestamo.getLibro());
            
            if (LocalDate.now().isAfter(prestamo.getFechaDevolucion())) {
                Usuario usuario = prestamo.getUsuario();
                usuario.setFechaSancion(LocalDate.now().plusWeeks(1));
                usuarioRepository.save(usuario);
            }
            prestamoRepository.save(prestamo);
        }
    }
    
    public void ampliarPrestamo(Long prestamoId) {
        Optional<Prestamo> optionalPrestamo = prestamoRepository.findById(prestamoId);
        if (optionalPrestamo.isPresent() && !optionalPrestamo.get().isDevuelto()) {
            Prestamo prestamo = optionalPrestamo.get();
            prestamo.setFechaDevolucion(prestamo.getFechaDevolucion().plusWeeks(1));
            prestamoRepository.save(prestamo);
        }
    }
}
