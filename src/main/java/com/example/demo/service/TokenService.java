package com.example.demo.service;

import com.example.demo.entity.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.segredo}")
    private String segredo;

    public String gerarToken(Usuario usuario) {
        Instant agora = Instant.now();
        Instant expiracao = agora.plus(1, ChronoUnit.DAYS);

        return Jwts.builder()
                .setSubject(usuario.getId().toString())
                .claim("nome", usuario.getNome())
                .setIssuedAt(Date.from(agora))
                .setExpiration(Date.from(expiracao))
                .signWith(Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
