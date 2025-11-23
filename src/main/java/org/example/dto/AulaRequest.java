package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaRequest {

    @NotBlank(message = "Status é obrigatório")
    @Size(max = 20, message = "Status deve ter no máximo 20 caracteres")
    private String status;

    @Size(max = 200, message = "Link deve ter no máximo 200 caracteres")
    private String link;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;

    @Positive(message = "Quantidade de horas deve ser positiva")
    private BigDecimal quantidadeHoras;

    @NotNull(message = "ID da modalidade é obrigatório")
    private Integer modalidadeId;

    @NotNull(message = "ID do professor é obrigatório")
    private Integer professorId;

    @NotNull(message = "ID do aluno é obrigatório")
    private Integer alunoId;
}
