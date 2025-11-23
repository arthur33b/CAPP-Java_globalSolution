package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AlunoRequest;
import org.example.dto.AlunoResponse;
import org.example.entity.Aluno;
import org.example.repository.AlunoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<AlunoResponse> findAll() {
        return alunoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlunoResponse findById(Integer id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));
        return toResponse(aluno);
    }

    @Transactional
    public AlunoResponse create(AlunoRequest request) {
        Integer maxId = alunoRepository.findAll().stream()
                .map(Aluno::getId)
                .max(Integer::compareTo)
                .orElse(0);

        Aluno aluno = new Aluno();
        aluno.setId(maxId + 1);
        aluno.setNome(request.getNome());
        aluno.setEmail(request.getEmail());
        aluno.setSenha(passwordEncoder.encode(request.getSenha())); // Criptografa a senha
        aluno.setTelefone(request.getTelefone());

        Aluno saved = alunoRepository.save(aluno);
        return toResponse(saved);
    }

    @Transactional
    public AlunoResponse update(Integer id, AlunoRequest request) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado com ID: " + id));

        aluno.setNome(request.getNome());
        aluno.setEmail(request.getEmail());
        aluno.setSenha(passwordEncoder.encode(request.getSenha())); // Criptografa a senha
        aluno.setTelefone(request.getTelefone());

        Aluno updated = alunoRepository.save(aluno);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!alunoRepository.existsById(id)) {
            throw new RuntimeException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }

    private AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getDataCadastro()
        );
    }
}
