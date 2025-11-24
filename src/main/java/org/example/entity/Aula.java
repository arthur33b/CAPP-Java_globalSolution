package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TB_CAPP_AULA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {

    @Id
    @Column(name = "ID_aula")
    private Integer id;

    @Column(name = "status_aula", nullable = false, length = 20)
    private String status;

    @Column(name = "Link_aula", length = 200)
    private String link;

    @Column(name = "preco_aula", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "qtd_hrs", precision = 4, scale = 2)
    private BigDecimal quantidadeHoras;

    @ManyToOne
    @JoinColumn(name = "TB_CAPP_MODALIDADE_ID_modalidade", nullable = false)
    private Modalidade modalidade;

    @ManyToOne
    @JoinColumn(name = "TB_CAPP_PROFESSOR_ID_professor", nullable = false)
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "TB_CAPP_ALUNO_ID_aluno", nullable = false)
    private Aluno aluno;

    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL)
    private List<Avaliacao> avaliacoes;
}
