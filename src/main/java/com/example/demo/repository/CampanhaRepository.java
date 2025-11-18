package com.example.demo.repository;

import com.example.demo.entity.Campanha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampanhaRepository extends JpaRepository<Campanha, Long> {

    List<Campanha> findByUsuarioId(Long usuarioId);
}
