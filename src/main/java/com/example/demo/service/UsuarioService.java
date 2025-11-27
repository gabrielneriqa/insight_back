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

    public Usuario buscarPorId(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }


    public Usuario atualizarNome(Long id, String novoNome) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(novoNome);
        return repositorio.save(usuario);
    }



    public Usuario atualizarEmail(Long id, String novoEmail) {

        // Verifica se o email já está em uso por outro usuário
        repositorio.findByEmail(novoEmail).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já está em uso");
            }
        });

        Usuario usuario = buscarPorId(id);
        usuario.setEmail(novoEmail);
        return repositorio.save(usuario);
    }

    public Usuario atualizarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorId(id);

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        return repositorio.save(usuario);
    }



}
