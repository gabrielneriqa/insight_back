package com.example.demo.controller;

import com.example.demo.dto.AtualizarCampoDTO;
import com.example.demo.dto.AtualizarSenhaDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/update")
@CrossOrigin("*")
public class UsuarioController {
    private final UsuarioRepository usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public UsuarioController(UsuarioRepository usuarioRepositorio,
                             PasswordEncoder passwordEncoder,
                             TokenService tokenService) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    @PatchMapping("/{id}/nome")
    public ResponseEntity<Usuario> atualizarNome(
            @PathVariable Long id,
            @RequestBody AtualizarCampoDTO dto
    ) {

        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setNome(dto.getValor());
        Usuario salvo = usuarioRepositorio.save(usuario);

        return ResponseEntity.ok(salvo);
    }




    // ============================
    // ATUALIZAR EMAIL
    // ============================
    @PatchMapping("/{id}/email")
    public ResponseEntity<Usuario> atualizarEmail(
            @PathVariable Long id,
            @RequestBody AtualizarCampoDTO dto
    ) {
        String novoEmail = dto.getValor();

        // Impede duplicidade
        usuarioRepositorio.findByEmail(novoEmail).ifPresent(u -> {
            if (!u.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já está em uso");
            }
        });

        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        usuario.setEmail(novoEmail);
        Usuario salvo = usuarioRepositorio.save(usuario);

        return ResponseEntity.ok(salvo);
    }

    // ============================
    // ATUALIZAR SENHA
    // ============================
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Usuario> atualizarSenha(
            @PathVariable Long id,
            @RequestBody AtualizarSenhaDTO dto) {

        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        Usuario salvo = usuarioRepositorio.save(usuario);

        return ResponseEntity.ok(salvo);
    }


}
