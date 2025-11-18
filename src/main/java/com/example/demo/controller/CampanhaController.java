package com.example.demo.controller;

import com.example.demo.dto.CampanhaDTO;
import com.example.demo.entity.Campanha;
import com.example.demo.service.CampanhaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campanhas")
@CrossOrigin("*")
public class CampanhaController {

    private final CampanhaService servico;

    public CampanhaController(CampanhaService servico) {
        this.servico = servico;
    }

    @PostMapping
    public Campanha criar(@RequestBody CampanhaDTO dto) {
        return servico.criar(dto);
    }

    @GetMapping("/{id}")
    public Campanha buscarPorId(@PathVariable Long id) {
        return servico.buscarPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Campanha> listarPorUsuario(@PathVariable Long usuarioId) {
        return servico.listarPorUsuario(usuarioId);
    }
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        servico.deletar(id);
    }

}
