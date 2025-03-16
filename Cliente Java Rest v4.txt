package com.biblioteca.client;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;

@Service
public class BibliotecaClient {
    private final String API_URL = "http://localhost:8080";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Usuario> obtenerUsuarios() {
        ResponseEntity<List> response = restTemplate.getForEntity(API_URL + "/usuarios", List.class);
        return response.getBody();
    }

    public Usuario crearUsuario(Usuario usuario) {
        HttpEntity<Usuario> request = new HttpEntity<>(usuario);
        return restTemplate.postForObject(API_URL + "/usuarios", request, Usuario.class);
    }
    
    public void editarUsuario(Long usuarioId, Usuario usuario) {
        HttpEntity<Usuario> request = new HttpEntity<>(usuario);
        restTemplate.exchange(API_URL + "/usuarios/" + usuarioId, HttpMethod.PUT, request, Void.class);
    }
    
    public void eliminarUsuario(Long usuarioId) {
        restTemplate.delete(API_URL + "/usuarios/" + usuarioId);
    }

    public List<Libro> obtenerLibrosDisponibles() {
        ResponseEntity<List> response = restTemplate.getForEntity(API_URL + "/libros?disponibles=true", List.class);
        return response.getBody();
    }

    public Prestamo tomarPrestado(Usuario usuario, Libro libro) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<PrestamoRequest> request = new HttpEntity<>(new PrestamoRequest(usuario, libro), headers);
        ResponseEntity<Prestamo> response = restTemplate.exchange(
                API_URL + "/prestamos/tomar", HttpMethod.POST, request, Prestamo.class);
        return response.getBody();
    }

    public void devolverPrestamo(Long prestamoId) {
        restTemplate.put(API_URL + "/prestamos/devolver/" + prestamoId, null);
    }
    
    public void ampliarPrestamo(Long prestamoId) {
        restTemplate.put(API_URL + "/prestamos/ampliar/" + prestamoId, null);
    }
    
    public List<Prestamo> obtenerHistorial(Long usuarioId) {
        ResponseEntity<List> response = restTemplate.getForEntity(API_URL + "/prestamos/historial/" + usuarioId, List.class);
        return response.getBody();
    }
}