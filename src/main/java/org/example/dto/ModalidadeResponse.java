package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModalidadeResponse {
    private Integer id;
    private String nome;
    private String descricao;
    private Integer areaId;
    private String areaNome;
}
