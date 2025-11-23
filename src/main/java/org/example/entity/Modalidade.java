package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "TB_CAPP_MODALIDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Modalidade {

    @Id
    @Column(name = "ID_modalidade")
    private Integer id;

    @Column(name = "nome_modalidade", nullable = false, length = 100)
    private String nome;

    @Column(name = "descricao_modalidade", length = 200)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "TB_CAPP_AREA_ID_area", nullable = false)
    private Area area;

    @OneToMany(mappedBy = "modalidade", cascade = CascadeType.ALL)
    private List<Aula> aulas;
}
