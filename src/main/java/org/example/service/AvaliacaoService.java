package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AvaliacaoRequest;
import org.example.dto.AvaliacaoResponse;
import org.example.entity.Aula;
import org.example.entity.Avaliacao;
import org.example.repository.AulaRepository;
import org.example.repository.AvaliacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final AulaRepository aulaRepository;

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> findAll() {
        return avaliacaoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvaliacaoResponse findById(Integer id) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
        return toResponse(avaliacao);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoResponse> findByAulaId(Integer aulaId) {
        return avaliacaoRepository.findByAulaId(aulaId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AvaliacaoResponse create(AvaliacaoRequest request) {
        Aula aula = aulaRepository.findById(request.getAulaId())
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com ID: " + request.getAulaId()));

        Integer maxId = avaliacaoRepository.findAll().stream()
                .map(Avaliacao::getId)
                .max(Integer::compareTo)
                .orElse(0);

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(maxId + 1);
        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());
        avaliacao.setAula(aula);

        Avaliacao saved = avaliacaoRepository.save(avaliacao);
        return toResponse(saved);
    }

    @Transactional
    public AvaliacaoResponse update(Integer id, AvaliacaoRequest request) {
        Avaliacao avaliacao = avaliacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));

        Aula aula = aulaRepository.findById(request.getAulaId())
                .orElseThrow(() -> new RuntimeException("Aula não encontrada com ID: " + request.getAulaId()));

        avaliacao.setNota(request.getNota());
        avaliacao.setComentario(request.getComentario());
        avaliacao.setAula(aula);

        Avaliacao updated = avaliacaoRepository.save(avaliacao);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new RuntimeException("Avaliação não encontrada com ID: " + id);
        }
        avaliacaoRepository.deleteById(id);
    }

    private AvaliacaoResponse toResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataAvaliacao(),
                avaliacao.getAula().getId()
        );
    }
}
