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
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 dia
                .signWith(Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    //Extrai o "sub" (ID do usuário) do token
    public String getSubject(String token) {
        return getClaims(token).getSubject();
    }


    //Valida expiração e assinatura
    public boolean validarToken(String token, Usuario usuario) {
        Claims claims = getClaims(token);

        boolean expirado = claims.getExpiration().before(new Date());
        if (expirado) return false;

        String subject = claims.getSubject();
        return subject.equals(usuario.getId().toString());
    }

    //Extrai os Claims para uso interno
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(segredo.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
