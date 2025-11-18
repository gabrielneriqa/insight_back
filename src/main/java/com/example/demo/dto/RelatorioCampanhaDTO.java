package com.example.demo.dto;

public class RelatorioCampanhaDTO {

    private String nomeCampanha;
    private Long totalAlcance;
    private Long totalEngajamento;
    private Long totalCliques;
    private Long totalLeads;

    public RelatorioCampanhaDTO(
            String nomeCampanha,
            Long totalAlcance,
            Long totalEngajamento,
            Long totalCliques,
            Long totalLeads
    ) {
        this.nomeCampanha = nomeCampanha;
        this.totalAlcance = totalAlcance;
        this.totalEngajamento = totalEngajamento;
        this.totalCliques = totalCliques;
        this.totalLeads = totalLeads;
    }

    public String getNomeCampanha() {
        return nomeCampanha;
    }

    public Long getTotalAlcance() {
        return totalAlcance;
    }

    public Long getTotalEngajamento() {
        return totalEngajamento;
    }

    public Long getTotalCliques() {
        return totalCliques;
    }

    public Long getTotalLeads() {
        return totalLeads;
    }
}
