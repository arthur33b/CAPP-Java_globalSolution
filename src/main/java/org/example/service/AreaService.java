package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AreaRequest;
import org.example.dto.AreaResponse;
import org.example.entity.Area;
import org.example.repository.AreaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaService {

    private final AreaRepository areaRepository;

    @Transactional(readOnly = true)
    public Page<AreaResponse> findAll(Pageable pageable) {
        return areaRepository.findAll(pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AreaResponse> findAllNoPagination() {
        return areaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AreaResponse findById(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área não encontrada com ID: " + id));
        return toResponse(area);
    }

    @Transactional
    public AreaResponse create(AreaRequest request) {
        Integer maxId = areaRepository.findAll().stream()
                .map(Area::getId)
                .max(Integer::compareTo)
                .orElse(0);

        Area area = new Area();
        area.setId(maxId + 1);
        area.setNome(request.getNome());
        area.setDescricao(request.getDescricao());

        Area saved = areaRepository.save(area);
        return toResponse(saved);
    }

    @Transactional
    public AreaResponse update(Integer id, AreaRequest request) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Área não encontrada com ID: " + id));

        area.setNome(request.getNome());
        area.setDescricao(request.getDescricao());

        Area updated = areaRepository.save(area);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Integer id) {
        if (!areaRepository.existsById(id)) {
            throw new RuntimeException("Área não encontrada com ID: " + id);
        }
        areaRepository.deleteById(id);
    }

    private AreaResponse toResponse(Area area) {
        return new AreaResponse(
                area.getId(),
                area.getNome(),
                area.getDescricao()
        );
    }
}
