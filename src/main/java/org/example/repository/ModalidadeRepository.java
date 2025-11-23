package org.example.repository;

import org.example.entity.Modalidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModalidadeRepository extends JpaRepository<Modalidade, Integer> {
    List<Modalidade> findByAreaId(Integer areaId);
}
