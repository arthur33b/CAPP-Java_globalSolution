package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaResponse {
    private Integer id;
    private String status;
    private String link;
    private BigDecimal preco;
    private BigDecimal quantidadeHoras;
    private Integer modalidadeId;
    private String modalidadeNome;
    private Integer professorId;
    private String professorNome;
    private Integer alunoId;
    private String alunoNome;
}
