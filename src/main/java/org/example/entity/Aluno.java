package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_COURAPP_ALUNO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    @Column(name = "ID_aluno")
    private Integer id;

    @Column(name = "nome_aluno", nullable = false, length = 100)
    private String nome;

    @Column(name = "email_aluno", nullable = false, length = 50)
    private String email;

    @Column(name = "senha_aluno", nullable = false, length = 50)
    private String senha;

    @Column(name = "telefone_aluno", length = 20)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Aula> aulas;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
    }
}
