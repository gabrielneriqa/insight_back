package com.example.demo.repository;

import com.example.demo.entity.ResultadoCampanha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface ResultadoCampanhaRepository extends JpaRepository<ResultadoCampanha, Long> {

    List<ResultadoCampanha> findByCampanhaIdAndDataBetween(
            Long campanhaId,
            LocalDate dataInicio,
            LocalDate dataFim
    );

    @Modifying
    @Transactional
    void deleteByCampanhaId(Long campanhaId);
}
