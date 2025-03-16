package com.biblioteca.client;

public class Main {
    public static void main(String[] args) {
        BibliotecaClient client = new BibliotecaClient();

        // Obtener usuarios
        System.out.println("Lista de usuarios: " + client.obtenerUsuarios());

        // Crear un nuevo usuario
        Usuario nuevoUsuario = new Usuario(null, "Ana López", "789123", null, "ana@example.com", null);
        Usuario usuarioCreado = client.crearUsuario(nuevoUsuario);
        System.out.println("Usuario Creado: " + usuarioCreado);
    }
}