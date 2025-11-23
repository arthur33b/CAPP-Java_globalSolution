package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.ModalidadeRequest;
import org.example.dto.ModalidadeResponse;
import org.example.entity.Area;
import org.example.entity.Modalidade;
import org.example.repository.AreaRepository;
import org.example.repository.ModalidadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModalidadeService {

    private final ModalidadeRepository modalidadeRepository;
    private final AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public List<ModalidadeResponse> findAll() {
        return modalidadeRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ModalidadeResponse findById(Integer id) {
        Modalidade modalidade = modalidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada com ID: " + id));
        return toResponse(modalidade);
    }

    @Transactional(readOnly = true)
    public List<ModalidadeResponse> findByAreaId(Integer areaId) {
        return modalidadeRepository.findByAreaId(areaId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ModalidadeResponse create(ModalidadeRequest request) {
        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área não encontrada com ID: " + request.getAreaId()));

        Integer maxId = modalidadeRepository.findAll().stream()
                .map(Modalidade::getId)
                .max(Integer::compareTo)
                .orElse(0);

        Modalidade modalidade = new Modalidade();
        modalidade.setId(maxId + 1);
        modalidade.setNome(request.getNome());
        modalidade.setDescricao(request.getDescricao());
        modalidade.setArea(area);

        Modalidade saved = modalidadeRepository.save(modalidade);
        return toResponse(saved);
    }

    @Transactional
    public ModalidadeResponse update(Integer id, ModalidadeRequest request) {
        Modalidade modalidade = modalidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Modalidade não encontrada com ID: " + id));

        Area area = areaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área não encontrada com ID: " + request.getAreaId()));

        modalidade.setNome(request.getNome());
        modalidade.setDescricao(request.getDescricao());
        modalidade.setArea(area);

        Modalidade updated = modalidadeRepository.save(modalidade);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!modalidadeRepository.existsById(id)) {
            throw new RuntimeException("Modalidade não encontrada com ID: " + id);
        }
        modalidadeRepository.deleteById(id);
    }

    private ModalidadeResponse toResponse(Modalidade modalidade) {
        return new ModalidadeResponse(
                modalidade.getId(),
                modalidade.getNome(),
                modalidade.getDescricao(),
                modalidade.getArea().getId(),
                modalidade.getArea().getNome()
        );
    }
}
