package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TB_CAPP_AVALIACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @Column(name = "ID_avaliacao")
    private Integer id;

    @Column(name = "nota", nullable = false)
    private Integer nota;

    @Column(name = "comentario", length = 100)
    private String comentario;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDate dataAvaliacao;

    @ManyToOne
    @JoinColumn(name = "TB_CAPP_AULA_ID_aula", nullable = false)
    private Aula aula;

    @PrePersist
    public void prePersist() {
        if (dataAvaliacao == null) {
            dataAvaliacao = LocalDate.now();
        }
    }
}
