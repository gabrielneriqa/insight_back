package com.example.demo.service;

import com.example.demo.dto.ResultadoCampanhaDTO;
import com.example.demo.dto.RelatorioCampanhaDTO;
import com.example.demo.entity.Campanha;
import com.example.demo.entity.ResultadoCampanha;
import com.example.demo.repository.CampanhaRepository;
import com.example.demo.repository.ResultadoCampanhaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ResultadoCampanhaService {

    private final CampanhaRepository campanhaRepositorio;
    private final ResultadoCampanhaRepository resultadoRepositorio;

    public ResultadoCampanhaService(
            CampanhaRepository campanhaRepositorio,
            ResultadoCampanhaRepository resultadoRepositorio
    ) {
        this.campanhaRepositorio = campanhaRepositorio;
        this.resultadoRepositorio = resultadoRepositorio;
    }

    public ResultadoCampanha salvar(ResultadoCampanhaDTO dto) {

        Campanha campanha = campanhaRepositorio.findById(dto.getCampanhaId())
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));

        ResultadoCampanha resultado = new ResultadoCampanha(
                campanha,
                dto.getAlcance(),
                dto.getEngajamento(),
                dto.getCliques(),
                dto.getLeads(),
                dto.getData()
        );

        return resultadoRepositorio.save(resultado);
    }

    public RelatorioCampanhaDTO gerarRelatorioPorCampanha(
            Long campanhaId,
            LocalDate inicio,
            LocalDate fim
    ) {
        List<ResultadoCampanha> resultados =
                resultadoRepositorio.findByCampanhaIdAndDataBetween(campanhaId, inicio, fim);

        Long totalAlcance = resultados.stream().mapToLong(ResultadoCampanha::getAlcance).sum();
        Long totalEngajamento = resultados.stream().mapToLong(ResultadoCampanha::getEngajamento).sum();
        Long totalCliques = resultados.stream().mapToLong(ResultadoCampanha::getCliques).sum();
        Long totalLeads = resultados.stream().mapToLong(ResultadoCampanha::getLeads).sum();

        String nomeCampanha = resultados.isEmpty()
                ? "Campanha sem resultados"
                : resultados.get(0).getCampanha().getNome();

        return new RelatorioCampanhaDTO(
                nomeCampanha,
                totalAlcance,
                totalEngajamento,
                totalCliques,
                totalLeads
        );
    }
}
