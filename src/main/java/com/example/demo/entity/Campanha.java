package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "campanhas")
public class Campanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;   // <-- ESTE CARA PRECISA EXISTIR

    public Campanha() {}

    public Campanha(String nome, Usuario usuario) {
        this.nome = nome;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Usuario getUsuario() {     // getter
        return usuario;
    }

    public void setUsuario(Usuario usuario) {   // setter
        this.usuario = usuario;
    }
}
