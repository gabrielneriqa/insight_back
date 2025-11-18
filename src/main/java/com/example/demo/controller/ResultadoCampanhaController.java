package com.example.demo.controller;

import com.example.demo.dto.ResultadoCampanhaDTO;
import com.example.demo.dto.RelatorioCampanhaDTO;
import com.example.demo.entity.ResultadoCampanha;
import com.example.demo.service.ResultadoCampanhaService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/resultados")
@CrossOrigin("*")
public class ResultadoCampanhaController {

    private final ResultadoCampanhaService servico;

    public ResultadoCampanhaController(ResultadoCampanhaService servico) {
        this.servico = servico;
    }

    @PostMapping
    public ResultadoCampanha salvar(@RequestBody ResultadoCampanhaDTO dto) {
        return servico.salvar(dto);
    }

    @GetMapping("/relatorio/{campanhaId}")
    public RelatorioCampanhaDTO gerarRelatorio(@PathVariable Long campanhaId) {
        LocalDate fim = LocalDate.now();
        LocalDate inicio = fim.minusDays(30);

        return servico.gerarRelatorioPorCampanha(campanhaId, inicio, fim);
    }
}
