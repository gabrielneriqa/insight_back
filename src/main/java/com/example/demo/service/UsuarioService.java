package com.example.demo.service;

import com.example.demo.dto.UsuarioCadastroDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository repositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repositorio,
                          PasswordEncoder passwordEncoder) {
        this.repositorio = repositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = repositorio.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha inválida");
        }

        return usuario;
    }

    public Usuario cadastrar(UsuarioCadastroDTO dto) {
        boolean emailJaExiste = repositorio.findByEmail(dto.getEmail()).isPresent();
        if (emailJaExiste) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        }

        Usuario novo = new Usuario();
        novo.setNome(dto.getNome());
        novo.setEmail(dto.getEmail());
        novo.setSenha(passwordEncoder.encode(dto.getSenha()));

        return repositorio.save(novo);
    }
}
