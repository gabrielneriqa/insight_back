package com.example.demo.service;

import com.example.demo.dto.CampanhaDTO;
import com.example.demo.entity.Campanha;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.CampanhaRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ResultadoCampanhaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampanhaService {

    private final CampanhaRepository campanhaRepositorio;
    private final UsuarioRepository usuarioRepositorio;
    private final ResultadoCampanhaRepository resultadoRepositorio;

    public CampanhaService(CampanhaRepository campanhaRepositorio,
                           UsuarioRepository usuarioRepositorio,
                           ResultadoCampanhaRepository resultadoRepositorio) {
        this.campanhaRepositorio = campanhaRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.resultadoRepositorio = resultadoRepositorio;
    }

    public Campanha criar(CampanhaDTO dto) {
        Usuario usuario = usuarioRepositorio.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Campanha campanha = new Campanha();
        campanha.setNome(dto.getNome());
        campanha.setUsuario(usuario);

        return campanhaRepositorio.save(campanha);
    }

    public Campanha buscarPorId(Long id) {
        return campanhaRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Campanha não encontrada"));
    }

    public List<Campanha> listarPorUsuario(Long usuarioId) {
        return campanhaRepositorio.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void deletar(Long id) {
        if (!campanhaRepositorio.existsById(id)) {
            throw new RuntimeException("Campanha não encontrada");
        }

        resultadoRepositorio.deleteByCampanhaId(id);

        campanhaRepositorio.deleteById(id);
    }

}
