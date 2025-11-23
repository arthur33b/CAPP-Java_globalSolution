package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoResponse {
    private Integer id;
    private Integer nota;
    private String comentario;
    private LocalDate dataAvaliacao;
    private Integer aulaId;
}
