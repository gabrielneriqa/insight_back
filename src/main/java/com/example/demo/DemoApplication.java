package com.example.demo;

import com.example.demo.entity.Campanha;
import com.example.demo.entity.ResultadoCampanha;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CampanhaRepository;
import com.example.demo.repository.ResultadoCampanhaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepositorio,
                           CampanhaRepository campanhaRepositorio,
                           ResultadoCampanhaRepository resultadoRepositorio,
                           PasswordEncoder passwordEncoder) {
        return args -> {

            // ---------- USUÁRIOS INICIAIS ----------

            // Usuário 1: Administrador
            Usuario usuario1 = usuarioRepositorio.findByEmail("admin@insighttrack.com")
                    .orElseGet(() -> {
                        Usuario u = new Usuario(
                                "Administrador",
                                "admin@insighttrack.com",
                                passwordEncoder.encode("123456")
                        );
                        return usuarioRepositorio.save(u);
                    });

            // Usuário 2: Gestor
            Usuario usuario2 = usuarioRepositorio.findByEmail("gestor@insighttrack.com")
                    .orElseGet(() -> {
                        Usuario u = new Usuario(
                                "Gestor de Marketing",
                                "gestor@insighttrack.com",
                                passwordEncoder.encode("123456")
                        );
                        return usuarioRepositorio.save(u);
                    });

            // ---------- CAMPANHAS + RESULTADOS DO USUÁRIO 1 ----------

            List<Campanha> campanhasUsuario1 = campanhaRepositorio.findByUsuarioId(usuario1.getId());

            if (campanhasUsuario1.isEmpty()) {
                Campanha c1 = new Campanha("Campanha Instagram - Admin", usuario1);
                Campanha c2 = new Campanha("Campanha Google Ads - Admin", usuario1);

                c1 = campanhaRepositorio.save(c1);
                c2 = campanhaRepositorio.save(c2);

                // resultados para c1
                ResultadoCampanha r1 = new ResultadoCampanha(
                        c1,
                        30000, 2000, 800, 200,
                        LocalDate.now().minusDays(10)
                );
                ResultadoCampanha r2 = new ResultadoCampanha(
                        c1,
                        45000, 2800, 1200, 320,
                        LocalDate.now().minusDays(5)
                );

                // resultados para c2
                ResultadoCampanha r3 = new ResultadoCampanha(
                        c2,
                        50000, 3200, 1500, 400,
                        LocalDate.now().minusDays(7)
                );

                resultadoRepositorio.save(r1);
                resultadoRepositorio.save(r2);
                resultadoRepositorio.save(r3);
            }

            // ---------- CAMPANHAS + RESULTADOS DO USUÁRIO 2 ----------

            List<Campanha> campanhasUsuario2 = campanhaRepositorio.findByUsuarioId(usuario2.getId());

            if (campanhasUsuario2.isEmpty()) {
                Campanha c3 = new Campanha("Campanha Facebook - Gestor", usuario2);
                Campanha c4 = new Campanha("Campanha LinkedIn - Gestor", usuario2);

                c3 = campanhaRepositorio.save(c3);
                c4 = campanhaRepositorio.save(c4);

                ResultadoCampanha r4 = new ResultadoCampanha(
                        c3,
                        20000, 1500, 600, 150,
                        LocalDate.now().minusDays(12)
                );
                ResultadoCampanha r5 = new ResultadoCampanha(
                        c3,
                        28000, 1900, 750, 210,
                        LocalDate.now().minusDays(3)
                );
                ResultadoCampanha r6 = new ResultadoCampanha(
                        c4,
                        18000, 900, 400, 80,
                        LocalDate.now().minusDays(8)
                );

                resultadoRepositorio.save(r4);
                resultadoRepositorio.save(r5);
                resultadoRepositorio.save(r6);
            }
        };
    }
}
