package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponse {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataCadastro;
}
