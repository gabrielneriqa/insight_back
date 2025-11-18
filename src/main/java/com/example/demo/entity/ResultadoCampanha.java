package com.example.demo.entity;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "resultados_campanha")
public class ResultadoCampanha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "campanha_id", nullable = false)
    private Campanha campanha;

    private Integer alcance;
    private Integer engajamento;
    private Integer cliques;
    private Integer leads;

    private LocalDate data;

    public ResultadoCampanha() {}

    public ResultadoCampanha(
            Campanha campanha,
            Integer alcance,
            Integer engajamento,
            Integer cliques,
            Integer leads,
            LocalDate data
    ) {
        this.campanha = campanha;
        this.alcance = alcance;
        this.engajamento = engajamento;
        this.cliques = cliques;
        this.leads = leads;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public Campanha getCampanha() {
        return campanha;
    }

    public void setCampanha(Campanha campanha) {
        this.campanha = campanha;
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