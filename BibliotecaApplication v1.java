package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class BibliotecaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
    }
}

// Modelo de Usuario
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String matricula;
    private LocalDate fechaNacimiento;
    private String correo;
    private LocalDate fechaSancion;
}

// Modelo de Libro
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String autor;
    private String edicion;
    private String ISBN;
    private String editorial;
    private boolean disponible = true;
}

// Modelo de Préstamo
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Libro libro;
    
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private boolean devuelto = false;
}

// Repositorios
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findAll(Pageable pageable);
}

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Page<Libro> findByDisponibleTrue(Pageable pageable);
    Page<Libro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    Page<Prestamo> findByUsuarioId(Long usuarioId, Pageable pageable);
}

// Servicio para Préstamos
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
    
    public Page<Prestamo> obtenerHistorial(Long usuarioId, Pageable pageable) {
        return prestamoRepository.findByUsuarioId(usuarioId, pageable);
    }
}

// Controlador REST para Préstamos
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
    
    @GetMapping("/historial/{usuarioId}")
    public Page<Prestamo> obtenerHistorial(@PathVariable Long usuarioId, Pageable pageable) {
        return prestamoService.obtenerHistorial(usuarioId, pageable);
    }
}
