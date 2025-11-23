package org.example.repository;

import org.example.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
    List<Aula> findByAlunoId(Integer alunoId);
    List<Aula> findByProfessorId(Integer professorId);
    List<Aula> findByModalidadeId(Integer modalidadeId);
    List<Aula> findByStatus(String status);
}
