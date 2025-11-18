package com.example.demo.dto;

public class LoginResponseDTO {

    private String token;
    private Long usuarioId;
    private String nome;

    public LoginResponseDTO(String token, Long usuarioId, String nome) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nome = nome;
    }

    public String getToken() {
        return token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNome() {
        return nome;
    }
}
