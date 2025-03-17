package com.biblioteca.repository;

import com.biblioteca.model.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    Page<Prestamo> findByUsuarioId(Long usuarioId, Pageable pageable);
    List<Prestamo> findByUsuarioId(Long usuarioId);  // ← Este es el nuevo método
}

