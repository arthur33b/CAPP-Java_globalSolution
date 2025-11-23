package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AulaRequest;
import org.example.dto.AulaResponse;
import org.example.entity.Aluno;
import org.example.entity.Aula;
import org.example.entity.Modalidade;
import org.example.entity.Professor;
import org.example.repository.AlunoRepository;
import org.example.repository.AulaRepository;
import org.example.repository.ModalidadeRepository;
import org.example.repository.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AulaService {

    private final AulaRepository aulaRepository;
    private final ModalidadeRepository modalidadeRepository;
    private final ProfessorRepository professorRepository;
    private final AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public List<AulaResponse> findAll() {
        return aulaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AulaResponse findById(Integer id) {
        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com ID: " + id));
        return toResponse(aula);
    }

    @Transactional(readOnly = true)
    public List<AulaResponse> findByAlunoId(Integer alunoId) {
        return aulaRepository.findByAlunoId(alunoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AulaResponse> findByProfessorId(Integer professorId) {
        return aulaRepository.findByProfessorId(professorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AulaResponse> findByModalidadeId(Integer modalidadeId) {
        return aulaRepository.findByModalidadeId(modalidadeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AulaResponse> findByStatus(String status) {
        return aulaRepository.findByStatus(status).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AulaResponse create(AulaRequest request) {
        Modalidade modalidade = modalidadeRepository.findById(request.getModalidadeId())
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada com ID: " + request.getModalidadeId()));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + request.getProfessorId()));

        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + request.getAlunoId()));

        Integer maxId = aulaRepository.findAll().stream()
                .map(Aula::getId)
                .max(Integer::compareTo)
                .orElse(0);

        Aula aula = new Aula();
        aula.setId(maxId + 1);
        aula.setStatus(request.getStatus());
        aula.setLink(request.getLink());
        aula.setPreco(request.getPreco());
        aula.setQuantidadeHoras(request.getQuantidadeHoras());
        aula.setModalidade(modalidade);
        aula.setProfessor(professor);
        aula.setAluno(aluno);

        Aula saved = aulaRepository.save(aula);
        return toResponse(saved);
    }

    @Transactional
    public AulaResponse update(Integer id, AulaRequest request) {
        Aula aula = aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com ID: " + id));

        Modalidade modalidade = modalidadeRepository.findById(request.getModalidadeId())
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada com ID: " + request.getModalidadeId()));

        Professor professor = professorRepository.findById(request.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + request.getProfessorId()));

        Aluno aluno = alunoRepository.findById(request.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + request.getAlunoId()));

        aula.setStatus(request.getStatus());
        aula.setLink(request.getLink());
        aula.setPreco(request.getPreco());
        aula.setQuantidadeHoras(request.getQuantidadeHoras());
        aula.setModalidade(modalidade);
        aula.setProfessor(professor);
        aula.setAluno(aluno);

        Aula updated = aulaRepository.save(aula);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!aulaRepository.existsById(id)) {
            throw new RuntimeException("Aula não encontrada com ID: " + id);
        }
        aulaRepository.deleteById(id);
    }

    private AulaResponse toResponse(Aula aula) {
        return new AulaResponse(
                aula.getId(),
                aula.getStatus(),
                aula.getLink(),
                aula.getPreco(),
                aula.getQuantidadeHoras(),
                aula.getModalidade().getId(),
                aula.getModalidade().getNome(),
                aula.getProfessor().getId(),
                aula.getProfessor().getNome(),
                aula.getAluno().getId(),
                aula.getAluno().getNome()
        );
    }
}
