package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponse {
    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataCadastro;
    private String bio;
    private String experiencia;
}
