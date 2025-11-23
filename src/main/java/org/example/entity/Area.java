package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_CAPP_AREA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {

    @Id
    @Column(name = "ID_area")
    private Integer id;

    @Column(name = "nome_area", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao_area", length = 100)
    private String descricao;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL)
    private List<Modalidade> modalidades;
}
