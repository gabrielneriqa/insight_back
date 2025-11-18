package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.UsuarioCadastroDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AutenticacaoController {

    private final UsuarioRepository usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AutenticacaoController(UsuarioRepository usuarioRepositorio,
                                  PasswordEncoder passwordEncoder,
                                  TokenService tokenService) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {

        Usuario usuario = usuarioRepositorio.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        String token = tokenService.gerarToken(usuario);

        LoginResponseDTO resposta = new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome()
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registrar(@RequestBody UsuarioCadastroDTO dto) {

        boolean emailJaExiste = usuarioRepositorio.findByEmail(dto.getEmail()).isPresent();
        if (emailJaExiste) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já cadastrado");
        }

        Usuario novo = new Usuario();
        novo.setNome(dto.getNome());
        novo.setEmail(dto.getEmail());
        novo.setSenha(passwordEncoder.encode(dto.getSenha()));

        Usuario salvo = usuarioRepositorio.save(novo);

        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}
