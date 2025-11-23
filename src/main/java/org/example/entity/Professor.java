package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_CAPP_PROFESSOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {

    @Id
    @Column(name = "ID_professor")
    private Integer id;

    @Column(name = "nome_professor", nullable = false, length = 100)
    private String nome;

    @Column(name = "email_professor", nullable = false, length = 50)
    private String email;

    @Column(name = "senha_professor", nullable = false, length = 50)
    private String senha;

    @Column(name = "telefone_professor", length = 20)
    private String telefone;

    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Column(name = "bio_professor", length = 200)
    private String bio;

    @Column(name = "experiencia_professor", length = 100)
    private String experiencia;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Aula> aulas;

    @PrePersist
    public void prePersist() {
        if (dataCadastro == null) {
            dataCadastro = LocalDate.now();
        }
    }
}
