package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PrestamoService {
    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;

    public Prestamo tomarPrestado(Long usuarioId, Long libroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Libro libro = libroRepository.findById(libroId)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (!libro.isDisponible()) {
            throw new RuntimeException("El libro no está disponible");
        }

        libro.setDisponible(false);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo(null, usuario, libro, LocalDate.now(), LocalDate.now().plusWeeks(2), false);
        return prestamoRepository.save(prestamo);
    }

    public void devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
            .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

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

    public List<Prestamo> obtenerHistorial(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }

    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }
    public void ampliarPrestamo(Long prestamoId) {
    Optional<Prestamo> optionalPrestamo = prestamoRepository.findById(prestamoId);
    if (optionalPrestamo.isPresent() && !optionalPrestamo.get().isDevuelto()) {
        Prestamo prestamo = optionalPrestamo.get();
        prestamo.setFechaDevolucion(prestamo.getFechaDevolucion().plusWeeks(1));
        prestamoRepository.save(prestamo);
    } else {
        throw new RuntimeException("Préstamo no encontrado o ya devuelto");
    }
}

}

