package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ProfessorRequest;
import org.example.dto.ProfessorResponse;
import org.example.entity.Professor;
import org.example.repository.ProfessorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<ProfessorResponse> findAll() {
        return professorRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfessorResponse findById(Integer id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));
        return toResponse(professor);
    }

    @Transactional
    public ProfessorResponse create(ProfessorRequest request) {
        Integer maxId = professorRepository.findAll().stream()
                .map(Professor::getId)
                .max(Integer::compareTo)
                .orElse(0);

        Professor professor = new Professor();
        professor.setId(maxId + 1);
        professor.setNome(request.getNome());
        professor.setEmail(request.getEmail());
        professor.setSenha(passwordEncoder.encode(request.getSenha())); // Criptografa a senha
        professor.setTelefone(request.getTelefone());
        professor.setBio(request.getBio());
        professor.setExperiencia(request.getExperiencia());

        Professor saved = professorRepository.save(professor);
        return toResponse(saved);
    }

    @Transactional
    public ProfessorResponse update(Integer id, ProfessorRequest request) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado com ID: " + id));

        professor.setNome(request.getNome());
        professor.setEmail(request.getEmail());
        professor.setSenha(passwordEncoder.encode(request.getSenha())); // Criptografa a senha
        professor.setTelefone(request.getTelefone());
        professor.setBio(request.getBio());
        professor.setExperiencia(request.getExperiencia());

        Professor updated = professorRepository.save(professor);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!professorRepository.existsById(id)) {
            throw new RuntimeException("Professor não encontrado com ID: " + id);
        }
        professorRepository.deleteById(id);
    }

    private ProfessorResponse toResponse(Professor professor) {
        return new ProfessorResponse(
                professor.getId(),
                professor.getNome(),
                professor.getEmail(),
                professor.getTelefone(),
                professor.getDataCadastro(),
                professor.getBio(),
                professor.getExperiencia()
        );
    }
}
