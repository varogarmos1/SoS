package com.biblioteca.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoRequest {
    private Long usuarioId;
    private Long libroId;
}
