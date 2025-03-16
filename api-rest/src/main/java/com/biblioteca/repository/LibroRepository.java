package com.biblioteca.repository;

import com.biblioteca.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Page<Libro> findByDisponibleTrue(Pageable pageable);
    Page<Libro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);
}
