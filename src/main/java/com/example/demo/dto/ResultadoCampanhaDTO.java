package com.example.demo.dto;

import java.time.LocalDate;

public class ResultadoCampanhaDTO {

    private Long campanhaId;
    private Integer alcance;
    private Integer engajamento;
    private Integer cliques;
    private Integer leads;
    private LocalDate data;

    public Long getCampanhaId() {
        return campanhaId;
    }

    public void setCampanhaId(Long campanhaId) {
        this.campanhaId = campanhaId;
    }

    public Integer getAlcance() {
        return alcance;
    }

    public void setAlcance(Integer alcance) {
        this.alcance = alcance;
    }

    public Integer getEngajamento() {
        return engajamento;
    }

    public void setEngajamento(Integer engajamento) {
        this.engajamento = engajamento;
    }

    public Integer getCliques() {
        return cliques;
    }

    public void setCliques(Integer cliques) {
        this.cliques = cliques;
    }

    public Integer getLeads() {
        return leads;
    }

    public void setLeads(Integer leads) {
        this.leads = leads;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
